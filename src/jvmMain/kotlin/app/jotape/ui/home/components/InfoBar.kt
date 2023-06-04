package app.jotape.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.jotape.services.GlobalService
import app.jotape.toBRDateTime
import app.jotape.version

@Composable
fun InfoBar(modifier: Modifier = Modifier) {
    val globalState by GlobalService.state.collectAsState()

    Column(modifier) {
        Divider()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
        ) {
            globalState.lastExec?.let { lastExec ->
                Text(
                    "Último ponto: ${lastExec.toBRDateTime()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider(Modifier.width(1.dp).fillMaxHeight())
            }
            globalState.nextExec?.let { nextExec ->
                Text(
                    "Próximo ponto: ${nextExec.toBRDateTime()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Divider(Modifier.width(1.dp).fillMaxHeight())
            }
            Text("Versão: $version", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}