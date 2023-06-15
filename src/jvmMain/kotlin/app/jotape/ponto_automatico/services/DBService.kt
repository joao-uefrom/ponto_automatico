package app.jotape.ponto_automatico.services

import app.jotape.ponto_automatico.models.Configuration
import app.jotape.ponto_automatico.models.Migration
import app.jotape.ponto_automatico.toSQLDateTime
import java.io.File
import java.sql.*

object DBService {
    fun execute(sql: String) = exec(sql, null)
    fun execute(sql: String, params: List<Any>) = exec(sql, params)

    fun <T> query(sql: String, result: (ResultSet) -> T): T = execQuery(sql, null, result)
    fun <T> query(sql: String, params: List<Any>, result: (ResultSet) -> T): T = execQuery(sql, params, result)

    fun init() {
        prepareDatabase()
        val lastMigration = getLastMigration()

        getMigrations().forEach { migration ->
            if (migration.statement > lastMigration) {
                val conn = connection()
                conn.autoCommit = false

                val stmt = conn.createStatement()
                stmt.executeUpdate("INSERT INTO migrations (statement) VALUES (${migration.statement})")
                stmt.execute(migration.sql)

                conn.commit()
                stmt.close()
                conn.close()
            }
        }
    }

    private fun connection(): Connection = DriverManager.getConnection("jdbc:sqlite:${dbPath()}")

    private fun dbPath(): String {
        val executablePath = File(System.getProperty("user.dir"))
        val dbPath = File(executablePath, "db.sqlite3")
        return dbPath.absolutePath
    }

    private fun getLastMigration(): Int = query("SELECT statement FROM migrations ORDER BY statement DESC LIMIT 1") {
        val lastMigration = if (it.next()) it.getInt("statement") else 0

        lastMigration
    }

    private fun getMigrations(): List<Migration> {
        return listOf(
            Migration(
                1,
                """
            CREATE TABLE configurations (
                key   TEXT NOT NULL UNIQUE,
                value TEXT NOT NULL
            )
            """.trimIndent()
            ),
            Migration(
                2,
                """
            CREATE TABLE schedules (hour TEXT NOT NULL UNIQUE)
            """.trimIndent()
            ),
            Migration(
                3,
                """
            CREATE TABLE logs (
                id         INTEGER PRIMARY KEY AUTOINCREMENT,
                class      TEXT NOT NULL,
                message    TEXT NOT NULL,
                type       TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """.trimIndent()
            )
        )
    }

    private fun prepareDatabase() = execute("CREATE TABLE IF NOT EXISTS migrations (statement INT NOT NULL UNIQUE)")

    private fun prepareStatement(sql: String, params: List<Any>, conn: Connection): PreparedStatement {
        val stmt = conn.prepareStatement(sql)

        params.forEachIndexed { index, any ->
            when (any) {
                is String -> stmt.setString(index + 1, any)
                is Int -> stmt.setInt(index + 1, any)
                is Boolean -> stmt.setString(index + 1, any.toString())
                is Configuration.DateTime -> stmt.setString(index + 1, any.value.toSQLDateTime())
                else -> throw Exception("Invalid type")
            }
        }

        return stmt
    }

    private fun exec(sql: String, params: List<Any>?) {
        val conn = connection()
        val stmt: Statement?

        if (params == null) {
            stmt = conn.createStatement()
            stmt.execute(sql)
        } else {
            stmt = prepareStatement(sql, params, conn)
            stmt.execute()
        }

        stmt!!.close()
        conn.close()
    }

    private fun <T> execQuery(sql: String, params: List<Any>?, result: (ResultSet) -> T): T {
        val conn = connection()
        val stmt: Statement?
        val resultSet: ResultSet?

        if (params == null) {
            stmt = conn.createStatement()
            resultSet = stmt.executeQuery(sql)
        } else {
            stmt = prepareStatement(sql, params, conn)
            resultSet = stmt.executeQuery()
        }

        val value = result(resultSet)

        resultSet.close()
        stmt!!.close()
        conn.close()

        return value
    }
}