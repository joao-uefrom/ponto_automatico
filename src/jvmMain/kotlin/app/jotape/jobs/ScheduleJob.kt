package app.jotape.jobs

import app.jotape.models.Configuration
import app.jotape.services.HttpService
import app.jotape.services.SchedulerService
import org.quartz.Job
import org.quartz.JobExecutionContext

class ScheduleJob : Job {

    override fun execute(context: JobExecutionContext) {
        Configuration.user()?.let {
            HttpService.login(it)
            HttpService.punchTheClock()
            HttpService.quit()
        }

        SchedulerService.toNext()
    }

}