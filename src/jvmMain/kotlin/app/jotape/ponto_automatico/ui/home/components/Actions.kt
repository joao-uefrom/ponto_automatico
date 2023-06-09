package app.jotape.ponto_automatico.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.jotape.ponto_automatico.services.GlobalService
import app.jotape.ponto_automatico.services.LogsService
import app.jotape.ponto_automatico.services.SchedulerService
import app.jotape.ponto_automatico.ui.home.HomeUIState
import app.jotape.ponto_automatico.ui.home.HomeViewModel

@Composable
fun Actions(uiState: HomeUIState, modifier: Modifier = Modifier) {
    val globalState by GlobalService.state.collectAsState()

    Column(modifier) {
        SectionTitle("Ações")

        Column(Modifier.padding(horizontal = 16.dp)) {
            Action(
                color = MaterialTheme.colors.primary,
                icon = Icons.Filled.AlarmOn,
                enabled = uiState.isLoading.not() && GlobalService.isValid(),
                onPressed = HomeViewModel::punchTheClock,
                fontSize = 12.sp,
                title = "Bater ponto"
            )

            if (GlobalService.canStop().not()) {
                Action(
                    color = Color(0xFF4CAF50),
                    icon = Icons.Filled.SmartToy,
                    enabled = GlobalService.canStart(),
                    onPressed = SchedulerService::run,
                    title = "Iniciar"
                )
            } else {
                Action(
                    color = Color.Red,
                    icon = Icons.Filled.SmartToy,
                    onPressed = SchedulerService::standby,
                    title = "Pausar"
                )
            }

            Action(
                color = Color(0xFFFFA500),
                icon = Icons.Filled.SkipPrevious,
                enabled = globalState.isRunning,
                onPressed = {
                    val nextExec = globalState.nextExec
                    if (nextExec != null) SchedulerService.toPrevious(nextExec)
                },
                title = "Voltar"
            )

            Action(
                color = Color(0xFF2383e9),
                icon = Icons.Filled.SkipNext,
                enabled = globalState.isRunning,
                onPressed = SchedulerService::toNext,
                title = "Próximo"
            )

            Action(
                color = Color.DarkGray,
                icon = Icons.Filled.DeleteForever,
                onPressed = LogsService::deleteAll,
                fontSize = 12.sp,
                title = "Apagar Logs"
            )
        }
    }
}

@Composable
private fun Action(
    color: Color,
    enabled: Boolean = true,
    icon: ImageVector,
    fontSize: TextUnit = TextUnit.Unspecified,
    onPressed: () -> Unit,
    title: String
) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        onClick = onPressed
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(icon, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, modifier = Modifier.weight(1f), fontSize = fontSize)
        }
    }
}
