package app.jotape

import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import app.jotape.data.Migrations
import app.jotape.services.GlobalService
import app.jotape.services.SchedulerService
import app.jotape.ui.TrayIcon
import app.jotape.ui.LoadingWindow
import app.jotape.ui.home.HomeWindow
import kotlinx.coroutines.delay

fun main() = application {
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        Migrations.migrate()

        GlobalService

        val schedulerService = SchedulerService
        schedulerService.tryRecover()

        delay(1000)
        isLoading = false
    }

    when (isLoading) {
        true -> LoadingWindow()
        false -> {
            TrayIcon(::exitApplication)
            HomeWindow(::exitApplication)
        }
    }
}

