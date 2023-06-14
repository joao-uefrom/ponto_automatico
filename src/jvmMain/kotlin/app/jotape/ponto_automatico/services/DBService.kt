package app.jotape.ponto_automatico.services

import app.jotape.ponto_automatico.models.Migration
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

object DBService {

    val connection: Connection by lazy { DriverManager.getConnection("jdbc:sqlite:${dbPath()}") }

    fun init() {
        prepareDatabase()
        val lastMigration = getLastMigration()

        getMigrations().forEach { migration ->
            if (migration.statement > lastMigration) {
                val conn = connection
                conn.autoCommit = false

                val stmt = conn.createStatement()
                stmt.executeUpdate("INSERT INTO migrations (statement) VALUES (${migration.statement})")
                stmt.execute(migration.sql)

                conn.commit()
                stmt.close()
            }
        }
    }

    private fun dbPath(): String {
        val executablePath = File(System.getProperty("user.dir"))
        val dbPath = File(executablePath, "db.sqlite3")
        return dbPath.absolutePath
    }

    private fun getLastMigration(): Int {
        val stmt = connection.createStatement()
        val result = stmt.executeQuery("SELECT statement FROM migrations ORDER BY statement DESC LIMIT 1")
        val lastMigration = if (result.next()) result.getInt("statement") else 0

        stmt.close()

        return lastMigration
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

    private fun prepareDatabase() {
        val stmt = connection.createStatement()
        stmt.execute("CREATE TABLE IF NOT EXISTS migrations (statement INT NOT NULL UNIQUE)")
        stmt.close()
    }
}