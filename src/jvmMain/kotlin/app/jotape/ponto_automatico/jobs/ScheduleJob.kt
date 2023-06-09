package app.jotape.ponto_automatico.jobs

import app.jotape.ponto_automatico.models.Configuration
import app.jotape.ponto_automatico.services.HttpService
import app.jotape.ponto_automatico.services.SchedulerService
import org.quartz.Job
import org.quartz.JobExecutionContext

class ScheduleJob : Job {

    private val httpService = HttpService()

    override fun execute(context: JobExecutionContext) {
        Configuration.user()?.let {
            httpService.login(it)
            httpService.punchTheClock()
            httpService.quit()
        }

        SchedulerService.toNext()
    }

}