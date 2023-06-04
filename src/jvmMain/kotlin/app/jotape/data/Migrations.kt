package app.jotape.data

import app.jotape.models.Migration

object Migrations {

    fun migrate() {
        prepareDatabase()
        val lastMigration = getLastMigration()

        migrations.forEach { migration ->
            if (migration.statement > lastMigration) {
                val conn = Database.connection
                conn.autoCommit = false

                val stmt = conn.createStatement()
                stmt.executeUpdate("INSERT INTO migrations (statement) VALUES (${migration.statement})")
                stmt.execute(migration.sql)

                conn.commit()
                stmt.close()
            }
        }
    }

    private val migrations = listOf(
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
                event      TEXT NOT NULL,
                message    TEXT NOT NULL,
                type       TEXT NOT NULL,
                created_at TEXT NOT NULL
            )
            """.trimIndent()
        )
    )

    private fun getLastMigration(): Int {
        val stmt = Database.connection.createStatement()
        val result = stmt.executeQuery("SELECT statement FROM migrations ORDER BY statement DESC LIMIT 1")
        val lastMigration = if (result.next()) result.getInt("statement") else 0

        stmt.close()

        return lastMigration
    }

    private fun prepareDatabase() {
        val stmt = Database.connection.createStatement()
        stmt.execute("CREATE TABLE IF NOT EXISTS migrations (statement INT NOT NULL UNIQUE)")
        stmt.close()
    }

}