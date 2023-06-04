package app.jotape.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.rememberTrayState
import app.jotape.appName
import app.jotape.services.GlobalService
import app.jotape.services.SchedulerService

@Composable
fun ApplicationScope.TrayIcon(
    exitApplication: () -> Unit
) {
    val globalState by GlobalService.state.collectAsState()
    val trayState = rememberTrayState()

    Tray(
        state = trayState,
        icon = TrayIcon,
        tooltip = appName,
        menu = {
            if (GlobalService.isValid())
                Item(
                    if (globalState.isRunning) "Pausar" else "Iniciar",
                    onClick = {
                        if (globalState.isRunning) {
                            SchedulerService.standby()
                        } else {
                            SchedulerService.run()
                        }
                    }
                )

            Item("Sair", onClick = ::exitApplication)
        }
    )
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}