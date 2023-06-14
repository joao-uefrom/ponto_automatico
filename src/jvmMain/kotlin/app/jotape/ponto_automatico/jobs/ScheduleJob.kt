package app.jotape.ponto_automatico.jobs

import app.jotape.ponto_automatico.models.Configuration
import app.jotape.ponto_automatico.services.HttpService
import app.jotape.ponto_automatico.services.SchedulerService
import org.quartz.Job
import org.quartz.JobExecutionContext

class ScheduleJob : Job {

    override fun execute(context: JobExecutionContext) {
        Configuration.user()?.let {
            if (HttpService.login(it)) {
                HttpService.punchTheClock()
            }

            HttpService.quit()
        }

        SchedulerService.toNext()
    }

}