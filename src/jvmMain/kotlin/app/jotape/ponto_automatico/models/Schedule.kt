package app.jotape.ponto_automatico.models

import app.jotape.ponto_automatico.services.DBService
import java.time.LocalTime

data class Schedule(val hour: LocalTime) {

    companion object {
        fun getAll(): List<Schedule> = DBService.query("SELECT hour FROM schedules") {
            val schedules = mutableListOf<Schedule>()

            while (it.next())
                schedules.add(Schedule(LocalTime.parse(it.getString("hour"))))

            schedules.sortedBy { schedule -> schedule.hour }
        }
    }

    fun delete() = DBService.execute("DELETE FROM schedules WHERE hour = ?", listOf(hour.toString()))

    fun exists(): Boolean = DBService.query("SELECT hour FROM schedules WHERE hour = ?", listOf(hour.toString())) {
        it.next()
    }

    fun insertUpdate() = DBService.execute(
        "INSERT INTO schedules (hour) VALUES (?) ON CONFLICT (hour) DO NOTHING",
        listOf(hour.toString())
    )
}