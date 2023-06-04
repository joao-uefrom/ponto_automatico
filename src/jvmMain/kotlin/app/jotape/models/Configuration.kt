package app.jotape.models

import app.jotape.data.Database

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
