package app.jotape.data

import java.sql.Connection
import java.sql.DriverManager

object Database {

    val connection: Connection by lazy {
        DriverManager.getConnection("jdbc:sqlite:${path()}")
    }

    private fun path(): String = "C:\\Users\\joao_\\Trabalhos\\pmweb\\ponto_automatico\\database.db"
}