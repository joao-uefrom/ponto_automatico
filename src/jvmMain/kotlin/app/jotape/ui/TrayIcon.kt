package app.jotape.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.window.*
import app.jotape.services.GlobalService

import app.jotape.appName

@Composable
fun ApplicationScope.TrayIcon() {
    val globalState by GlobalService.state.collectAsState()
    val trayState = rememberTrayState()
    val notification = rememberNotification("Notification", "Message from MyApp!", Notification.Type.Error)


    LaunchedEffect(Unit) {

    }

    Tray(
        state = trayState,
        icon = TrayIcon,
        tooltip = appName,
        menu = {

            if (globalState.isValid)
                Item(
                    if (globalState.isRunning) "Para" else "Iniciar",
                    onClick = {

                    }
                )

            Item(
                "Exit",
                onClick = {
                    trayState.sendNotification(notification)
                }
            )
        }
    )
}

object MyAppIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color.Green, Offset(size.width / 4, 0f), Size(size.width / 2f, size.height))
        drawOval(Color.Blue, Offset(0f, size.height / 4), Size(size.width, size.height / 2f))
        drawOval(Color.Red, Offset(size.width / 4, size.height / 4), Size(size.width / 2f, size.height / 2f))
    }
}

object TrayIcon : Painter() {
    override val intrinsicSize = Size(256f, 256f)

    override fun DrawScope.onDraw() {
        drawOval(Color(0xFFFFA500))
    }
}