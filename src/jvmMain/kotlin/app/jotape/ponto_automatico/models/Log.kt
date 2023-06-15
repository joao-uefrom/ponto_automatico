package app.jotape.ponto_automatico.models

import app.jotape.ponto_automatico.services.DBService
import app.jotape.ponto_automatico.dateTimeFormatterFromSQL
import app.jotape.ponto_automatico.toSQLDateTime
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
        fun deleteAll() = DBService.execute("DELETE FROM logs WHERE 1")

        fun getAll(): List<Log> =
            DBService.query("SELECT class, message, type, created_at FROM logs ORDER BY id DESC") {
                val logs = mutableListOf<Log>()

                while (it.next()) {
                    logs.add(
                        Log(
                            it.getString("class"),
                            it.getString("message"),
                            Type.valueOf(it.getString("type")),
                            LocalDateTime.parse(it.getString("created_at"), dateTimeFormatterFromSQL())
                        )
                    )
                }

                logs
            }
    }

    fun insert() =
        DBService.execute(
            "INSERT INTO logs (class, message, type, created_at) VALUES (?, ?, ?, ?)",
            listOf(
                `class`,
                message,
                type.toString(),
                createdAt.toSQLDateTime()
            )
        )
}
