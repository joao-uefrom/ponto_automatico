package app.jotape

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toBRDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à's' HH:mm")
    return this.format(formatter)
}

fun LocalDateTime.toSQLDateTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return this.format(formatter)
}

fun dateTimeFormatterFromSQL(): DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")