package app.jotape.models

import app.jotape.services.DBService
import app.jotape.dateTimeFormatterFromSQL
import app.jotape.toSQLDateTime
import java.time.LocalDateTime

data class Log(
    val `class`: String,
    val message: String,
    val type: Type,
    val createdAt: LocalDateTime
) {
    enum class Type {
        INFO,
        ERROR,
        WARNING
    }

    companion object {
        fun deleteAll() {
            val sql = "DELETE FROM logs WHERE 1"
            val stmt = DBService.connection.prepareStatement(sql)
            stmt.executeUpdate()
            stmt.close()
        }

        fun getAll(): List<Log> {
            val sql = "SELECT class, message, type, created_at FROM logs ORDER BY id DESC"
            val stmt = DBService.connection.prepareStatement(sql)

            val result = stmt.executeQuery()
            val logs = mutableListOf<Log>()

            while (result.next()) {
                logs.add(
                    Log(
                        result.getString("class"),
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

    fun insert() {
        val sql = "INSERT INTO logs (class, message, type, created_at) VALUES (?, ?, ?, ?)"
        val stmt = DBService.connection.prepareStatement(sql)

        stmt.setString(1, `class`)
        stmt.setString(2, message)
        stmt.setString(3, type.toString())
        stmt.setString(4, createdAt.toSQLDateTime())

        stmt.executeUpdate()
        stmt.close()
    }
}
