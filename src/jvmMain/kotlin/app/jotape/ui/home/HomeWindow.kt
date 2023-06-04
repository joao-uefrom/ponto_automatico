package app.jotape.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import app.jotape.appName
import app.jotape.height
import app.jotape.services.GlobalService
import app.jotape.titleBarHeight
import app.jotape.ui.Theme
import app.jotape.ui.TitleBar
import app.jotape.ui.home.components.*
import app.jotape.width

@Composable
fun HomeWindow(
    onCloseRequest: () -> Unit
) {
    val uiState by HomeViewModel.uiState.collectAsState()

    val state = rememberWindowState(
        width = width.dp,
        height = height.dp + titleBarHeight.dp,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    Window(
        state = state,
        onCloseRequest = onCloseRequest,
        undecorated = true,
        resizable = false
    ) {
        Theme {
            Column(Modifier.fillMaxSize()) {
                TitleBar(title = appName, onExit = onCloseRequest, onMinimize = { state.isMinimized = true })

                if (uiState.isLoading) LinearProgressIndicator(Modifier.fillMaxWidth())

                Row(Modifier.height(IntrinsicSize.Min)) {
                    AuthForm(uiState = uiState, modifier = Modifier.weight(1f).padding(bottom = 8.dp))

                    Divider(modifier = Modifier.width(1.dp).fillMaxHeight())

                    SchedulesForm(uiState = uiState, modifier = Modifier.weight(1f).padding(bottom = 8.dp))
                }

                Divider(modifier = Modifier.fillMaxWidth())

                Row(Modifier.weight(1f)) {
                    LogTable(uiState, Modifier.weight(.75f).fillMaxSize())

                    Divider(modifier = Modifier.width(1.dp).fillMaxHeight())

                    Actions(uiState, Modifier.weight(.25f).fillMaxSize())
                }

                InfoBar(Modifier.fillMaxWidth().height(24.dp))
            }
        }
    }
}