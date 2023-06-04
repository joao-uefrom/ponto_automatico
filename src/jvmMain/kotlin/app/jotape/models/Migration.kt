package app.jotape.models

data class Migration(
    val statement: Int,
    val sql: String
)
