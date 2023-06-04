package app.jotape.models

import app.jotape.data.Database
import app.jotape.dateTimeFormatterFromSQL
import app.jotape.toSQLDateTime
import java.time.LocalDateTime

data class Log(
    val event: String,
    val message: String,
    val type: Type,
    val createdAt: LocalDateTime
) {
    enum class Type {
        INFO,
        ERROR,
        WARNING
    }

    fun insert() {
        val sql = "INSERT INTO logs (event, message, type, created_at) VALUES (?, ?, ?, ?)"
        val stmt = Database.connection.prepareStatement(sql)

        stmt.setString(1, event)
        stmt.setString(2, message)
        stmt.setString(3, type.toString())
        stmt.setString(4, createdAt.toSQLDateTime())

        stmt.executeUpdate()
        stmt.close()
    }

    companion object {
        fun getAll(): List<Log> {
            val sql = "SELECT event, message, type, created_at FROM logs ORDER BY id DESC"
            val stmt = Database.connection.prepareStatement(sql)

            val result = stmt.executeQuery()
            val logs = mutableListOf<Log>()

            while (result.next()) {
                logs.add(
                    Log(
                        result.getString("event"),
                        result.getString("message"),
                        Type.valueOf(result.getString("type")),
                        LocalDateTime.parse(result.getString("created_at"), dateTimeFormatterFromSQL())
                    )
                )
            }

            stmt.close()

            return logs
        }
    }
}
