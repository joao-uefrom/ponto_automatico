package app.jotape.services

import app.jotape.jobs.ScheduleJob
import app.jotape.models.Configuration
import app.jotape.models.Schedule
import org.quartz.JobBuilder
import org.quartz.JobDetail
import org.quartz.TriggerBuilder
import org.quartz.TriggerKey.triggerKey
import org.quartz.impl.StdSchedulerFactory
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

object SchedulerService {

    private val scheduler = StdSchedulerFactory.getDefaultScheduler()
    private var jobDetail: JobDetail? = null

    fun run() {
        if (scheduler.isInStandbyMode.not()) return

        if (Schedule.getAll().isEmpty()) {
            LogsService.warning("SchedulerService", "Nenhum horário adicionado, impossível iniciar o serviço.")
            GlobalService.setRunning(false)
            return
        }

        scheduler.start()
        GlobalService.setRunning(true)

        LogsService.info(
            "SchedulerService",
            "Serviço iniciado em background."
        )
    }

    fun standby() {
        if (scheduler.isStarted && scheduler.isShutdown.not()) {
            scheduler.standby()
            GlobalService.setRunning(false)

            LogsService.info("SchedulerService", "Serviço removido de background.")
        }
    }

    fun toNext() {
        val schedules = Schedule.getAll()

        if (schedules.isEmpty()) {
            LogsService.warning("SchedulerService", "Nenhum horário encontrado, impossível agendar tarefa.")
            standby()
            return
        }

        val nextExec = Configuration.getNextExec()
        var now = when {
            nextExec == null || nextExec.isBefore(LocalDateTime.now()) -> LocalDateTime.now()
            else -> nextExec
        }
        val time = now.toLocalTime()

        for (schedule in schedules) {
            if (schedule.hour.isBefore(time) || schedule.hour == time) continue

            toSchedule(LocalDateTime.of(now.toLocalDate(), schedule.hour))
            return
        }

        // Ignorando final de semana
        now = now.plusDays(if (now.dayOfWeek == DayOfWeek.FRIDAY) 3 else 1)

        toSchedule(LocalDateTime.of(now.toLocalDate(), schedules.first().hour))
    }

    fun toPrevious(nextExec: LocalDateTime) {
        val schedules = Schedule.getAll()

        if (schedules.isEmpty()) {
            LogsService.warning("SchedulerService", "Nenhum horário encontrado, impossível agendar tarefa.")
            standby()
            return
        }

        if (nextExec.isBefore(LocalDateTime.now())) {
            LogsService.warning(
                "SchedulerService",
                "Não foi possível voltar, próxima execução não agendada ou já ocorreu."
            )
            return
        }

        val nowTime = nextExec.toLocalTime()
        var before: LocalTime? = null
        for (schedule in schedules) {
            if (schedule.hour.isBefore(nowTime)) {
                before = schedule.hour
                continue
            }
            break
        }

        if (before != null) {
            if (LocalDateTime.of(nextExec.toLocalDate(), before).isAfter(LocalDateTime.now())) {
                toSchedule(LocalDateTime.of(nextExec.toLocalDate(), before))
            } else {
                LogsService.warning("SchedulerService", "Não foi possível voltar, sem horário disponível.")
            }
            return
        }

        val oldExec = nextExec
            .minusDays(if (nextExec.dayOfWeek == DayOfWeek.MONDAY) 3 else 1)
            .withHour(23)
            .withMinute(59)
            .withSecond(59)

        toPrevious(oldExec)
    }

    fun tryRecover() {
        if (GlobalService.isRunning().not()) return

        val nextExec = Configuration.getNextExec()

        if (nextExec == null) {
            LogsService.info("SchedulerService", "Nada a ser recuperado.")
            GlobalService.setRunning(false)
            return
        }

        run()

        if (nextExec.isAfter(LocalDateTime.now())) {
            toSchedule(nextExec)
        } else {
            toNext()
        }
    }

    private fun getJob(): JobDetail {
        if (jobDetail == null)
            jobDetail = JobBuilder
                .newJob(ScheduleJob::class.java)
                .withIdentity("pontoJob")
                .build()

        return jobDetail!!
    }

    private fun toSchedule(dateTime: LocalDateTime) {
        val zoneId = ZoneId.systemDefault()
        val date = Date.from(dateTime.atZone(zoneId).toInstant())

        GlobalService.setNextExec(dateTime)

        val oldTrigger = scheduler.getTrigger(triggerKey("pontoTrigger"))
        val newTrigger = TriggerBuilder
            .newTrigger()
            .withIdentity("pontoTrigger")
            .startAt(date)
            .build()

        if (oldTrigger == null) {
            scheduler.scheduleJob(getJob(), newTrigger)
        } else {
            scheduler.rescheduleJob(triggerKey("pontoTrigger"), newTrigger)
        }

        LogsService.info("SchedulerService", "Próxima execução agendada para ${dateTime.toLocalTime()}.")
    }
}