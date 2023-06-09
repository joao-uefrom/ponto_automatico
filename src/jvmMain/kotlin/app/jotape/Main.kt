package app.jotape

import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import app.jotape.services.DBService
import app.jotape.services.GlobalService
import app.jotape.services.SchedulerService
import app.jotape.ui.LoadingWindow
import app.jotape.ui.TrayIcon
import app.jotape.ui.home.HomeWindow
import kotlinx.coroutines.delay

fun main() = application {
    var isLoading by remember { mutableStateOf(true) }
    val state by GlobalService.state.collectAsState()

    if (isLoading) LoadingWindow()

    LaunchedEffect(Unit) {
        DBService.init()
        GlobalService.init()
        SchedulerService.init()

        delay(1000)
        isLoading = false
    }

    if (isLoading.not()) {
        when (state.onBackground) {
            true -> TrayIcon(::exitApplication)
            false -> HomeWindow(::exitApplication)
        }
    }
}