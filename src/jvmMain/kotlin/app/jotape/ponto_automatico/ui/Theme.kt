package app.jotape.ponto_automatico.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

@Composable
fun Theme(content: @Composable () -> Unit) {
    val typography = Typography(
        defaultFontFamily = FontFamily(
            Font("font\\Segoe UI.ttf"),
            Font("font\\Segoe UI Bold.ttf", FontWeight.Bold),
            Font("font\\Segoe UI Bold Italic.ttf", FontWeight.Bold, FontStyle.Italic),
            Font("font\\Segoe UI Italic.ttf", FontWeight.Normal, FontStyle.Italic),
        )
    )

    MaterialTheme(colors = darkColors(background = Color.Black)) {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}