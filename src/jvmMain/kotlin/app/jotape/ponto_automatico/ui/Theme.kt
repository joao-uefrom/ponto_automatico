package app.jotape.ponto_automatico.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(colors = darkColors(background = Color.Black)) {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}