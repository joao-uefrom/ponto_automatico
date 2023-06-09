package app.jotape.ponto_automatico.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import app.jotape.ponto_automatico.appName
import app.jotape.ponto_automatico.height
import app.jotape.ponto_automatico.services.GlobalService
import app.jotape.ponto_automatico.titleBarHeight
import app.jotape.ponto_automatico.ui.Theme
import app.jotape.ponto_automatico.ui.TitleBar
import app.jotape.ponto_automatico.ui.home.components.*
import app.jotape.ponto_automatico.width

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
        undecorated = true
    ) {
        Theme {
            Column(Modifier.fillMaxSize()) {
                TitleBar(title = appName, onExit = onCloseRequest, onMinimizeRequest = { onMinimizeRequest(state) })

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

private fun onMinimizeRequest(state: WindowState) {
    state.isMinimized = true
    GlobalService.setOnBackground(true)
}