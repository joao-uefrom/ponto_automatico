package app.jotape.jobs

import org.quartz.Job
import org.quartz.JobExecutionContext

class ScheduleJob : Job {

    override fun execute(context: JobExecutionContext) {
        // println("ScheduleJob.execute()")
    }

}