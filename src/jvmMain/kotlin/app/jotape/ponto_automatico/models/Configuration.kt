package app.jotape.ponto_automatico.models

import app.jotape.ponto_automatico.dateTimeFormatterFromSQL
import app.jotape.ponto_automatico.services.DBService
import app.jotape.ponto_automatico.toSQLDateTime
import java.time.LocalDateTime

abstract class Configuration(
    private val key: Key,
    private val _value: Any
) {

    enum class Key {
        EMAIL,
        PASSWORD,
        TWO_FA,
        IS_VALID,
        IS_RUNNING,
        LAST_EXEC,
        NEXT_EXEC
    }

    class String(key: Key, value: kotlin.String) : Configuration(key, value)
    class DateTime(key: Key, val value: LocalDateTime) : Configuration(key, value)
    class Boolean(key: Key, val value: kotlin.Boolean) : Configuration(key, value)

    data class User(
        val email: kotlin.String,
        val password: kotlin.String,
        val twofa: kotlin.String,
        var isValid: kotlin.Boolean = false
    ) {
        companion object {
            fun delete() {
                delete(Key.EMAIL)
                delete(Key.PASSWORD)
                delete(Key.TWO_FA)
                delete(Key.IS_VALID)
            }
        }

        fun insertUpdate() {
            String(Key.EMAIL, email.trimIndent()).insertUpdate()
            String(Key.PASSWORD, password.trimIndent()).insertUpdate()
            String(Key.TWO_FA, twofa.trimIndent()).insertUpdate()
            Boolean(Key.IS_VALID, isValid).insertUpdate()
        }
    }

    companion object {
        fun isRunning(): Boolean? = get(Key.IS_RUNNING)?.let {
            return Boolean(Key.IS_RUNNING, it.toBoolean())
        }

        fun isValid(): Boolean? = get(Key.IS_VALID)?.let {
            return Boolean(Key.IS_VALID, it.toBoolean())
        }

        fun lastExec(): DateTime? = get(Key.LAST_EXEC)?.let {
            return DateTime(Key.LAST_EXEC, LocalDateTime.parse(it, dateTimeFormatterFromSQL()))
        }

        fun nextExec(): DateTime? = get(Key.NEXT_EXEC)?.let {
            val nextExec = LocalDateTime.parse(it, dateTimeFormatterFromSQL())
            val schedules = Schedule.getAll()

            if (schedules.isEmpty() ||
                schedules.find { schedule -> schedule.hour == nextExec.toLocalTime() } == null
            ) {
                delete(Key.NEXT_EXEC)
                return null
            }

            return DateTime(Key.NEXT_EXEC, nextExec)
        }

        fun user(): User? {
            val email = get(Key.EMAIL) ?: ""
            val password = get(Key.PASSWORD) ?: ""
            val twoFa = get(Key.TWO_FA) ?: ""

            return if (
                email.isNotEmpty() &&
                password.isNotEmpty() &&
                twoFa.isNotEmpty()
            ) {
                User(
                    email,
                    password,
                    twoFa,
                    isValid()?.value ?: false
                )
            } else null
        }

        private fun get(key: Key): kotlin.String? {
            val sql = "SELECT value FROM configurations WHERE key = ?"
            val stmt = DBService.connection.prepareStatement(sql)

            stmt.setString(1, key.toString())

            val result = stmt.executeQuery()
            val configuration = when (result.next()) {
                true -> result.getString("value")
                false -> null
            }

            stmt.close()

            return configuration
        }

        private fun delete(key: Key) {
            val sql = "DELETE FROM configurations WHERE key = ?"
            val stmt = DBService.connection.prepareStatement(sql)

            stmt.setString(1, key.toString())

            stmt.executeUpdate()
            stmt.close()
        }
    }

    fun delete() = delete(this.key)

    fun insertUpdate() {
        val sql = "INSERT INTO configurations (key, value) VALUES (?, ?) ON CONFLICT (key) DO UPDATE SET value = ?"
        val stmt = DBService.connection.prepareStatement(sql)

        stmt.setString(1, key.toString())

        when (this) {
            is DateTime -> {
                stmt.setString(2, value.toSQLDateTime())
                stmt.setString(3, value.toSQLDateTime())
            }

            else -> {
                stmt.setString(2, _value.toString())
                stmt.setString(3, _value.toString())
            }
        }


        stmt.executeUpdate()
        stmt.close()
    }

}
