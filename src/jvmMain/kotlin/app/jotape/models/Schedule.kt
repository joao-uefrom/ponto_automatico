package app.jotape.models

import app.jotape.data.Database
import java.time.LocalTime

data class Schedule(val hour: LocalTime) {

    companion object {
        fun getAll(): List<Schedule> {
            val sql = "SELECT hour FROM schedules"
            val stmt = Database.connection.createStatement()
            val result = stmt.executeQuery(sql)
            val schedules = mutableListOf<Schedule>()

            while (result.next())
                schedules.add(Schedule(LocalTime.parse(result.getString("hour"))))

            stmt.close()

            return schedules
        }
    }

    fun delete() {
        val sql = "DELETE FROM schedules WHERE hour = ?"
        val stmt = Database.connection.prepareStatement(sql)
        stmt.setString(1, hour.toString())
        stmt.executeUpdate()
        stmt.close()
    }

    fun exists(): Boolean {
        val sql = "SELECT hour FROM schedules WHERE hour = ?"
        val stmt = Database.connection.prepareStatement(sql)
        stmt.setString(1, hour.toString())
        val result = stmt.executeQuery()
        val exists = result.next()
        stmt.close()

        return exists
    }

    fun insertUpdate() {
        val sql = "INSERT INTO schedules (hour) VALUES (?) ON CONFLICT (hour) DO NOTHING"
        val stmt = Database.connection.prepareStatement(sql)
        stmt.setString(1, hour.toString())
        stmt.executeUpdate()
        stmt.close()
    }
}