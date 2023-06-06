package app.jotape.models

import app.jotape.data.Database
import app.jotape.dateTimeFormatterFromSQL
import java.time.LocalDateTime

data class Configuration(
    val key: Key,
    val value: String
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

    companion object {
        fun get(key: Key): Configuration? {
            val sql = "SELECT key, value FROM configurations WHERE key = ?"
            val stmt = Database.connection.prepareStatement(sql)

            stmt.setString(1, key.toString())

            val result = stmt.executeQuery()
            val configuration = when (result.next()) {
                true -> {
                    Configuration(
                        Key.valueOf(result.getString("key")),
                        result.getString("value")
                    )
                }

                false -> null
            }

            stmt.close()

            return configuration
        }

        fun getLastExec(): LocalDateTime? {
            get(Key.LAST_EXEC)?.let {
                return LocalDateTime.parse(it.value, dateTimeFormatterFromSQL())
            }

            return null
        }

        fun getNextExec(): LocalDateTime? {
            get(Key.NEXT_EXEC)?.let { conf ->
                val nextExec = LocalDateTime.parse(conf.value, dateTimeFormatterFromSQL())
                val schedules = Schedule.getAll()

                if (schedules.isEmpty() ||
                    schedules.find { it.hour == nextExec.toLocalTime() } == null
                ) {
                    Configuration(Key.NEXT_EXEC, "").delete()
                    return null
                }

                return nextExec
            }



            return null
        }
    }

    fun delete() {
        val sql = "DELETE FROM configurations WHERE key = ?"
        val stmt = Database.connection.prepareStatement(sql)

        stmt.setString(1, key.toString())

        stmt.executeUpdate()
        stmt.close()
    }

    fun insertUpdate() {
        val sql = "INSERT INTO configurations (key, value) VALUES (?, ?) ON CONFLICT (key) DO UPDATE SET value = ?"
        val stmt = Database.connection.prepareStatement(sql)

        stmt.setString(1, key.toString())
        stmt.setString(2, value)
        stmt.setString(3, value)

        stmt.executeUpdate()
        stmt.close()
    }

}
