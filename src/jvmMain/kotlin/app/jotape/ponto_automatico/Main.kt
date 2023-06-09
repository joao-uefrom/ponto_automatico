package app.jotape.ponto_automatico

import androidx.compose.runtime.*
import androidx.compose.ui.window.application
import app.jotape.ponto_automatico.services.DBService
import app.jotape.ponto_automatico.services.GlobalService
import app.jotape.ponto_automatico.services.SchedulerService
import app.jotape.ponto_automatico.ui.LoadingWindow
import app.jotape.ponto_automatico.ui.TrayIcon
import app.jotape.ponto_automatico.ui.home.HomeWindow
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