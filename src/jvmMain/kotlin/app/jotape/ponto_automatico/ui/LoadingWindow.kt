package app.jotape.ponto_automatico.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState

@Composable
fun LoadingWindow() {
    Window(
        onCloseRequest = {},
        state = rememberWindowState(width = 420.dp, height = 150.dp, position = WindowPosition.Aligned(Alignment.Center)),
        undecorated = true,
        resizable = false
    ) {
        Column {
            TitleBar(title = "Ponto Automático", centerTitle = true)

            Theme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Carregando...", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}