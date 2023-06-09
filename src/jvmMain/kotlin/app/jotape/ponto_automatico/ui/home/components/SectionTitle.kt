package app.jotape.ponto_automatico.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        Divider()
    }
}