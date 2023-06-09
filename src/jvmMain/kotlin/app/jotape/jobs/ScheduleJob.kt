package app.jotape.jobs

import app.jotape.models.Configuration
import app.jotape.services.HttpService
import app.jotape.services.SchedulerService
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