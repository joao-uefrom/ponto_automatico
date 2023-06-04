package app.jotape.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NextPlan
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import app.jotape.services.GlobalService
import app.jotape.ui.home.HomeUIState

@Composable
fun Actions(uiState: HomeUIState, modifier: Modifier = Modifier) {
    val globalState by GlobalService.state.collectAsState()

    Column(modifier) {
        SectionTitle("Ações")

        Column(
            Modifier.padding(horizontal = 16.dp)
        ) {
            if (globalState.isRunning.not()) {
                Action(
                    color = Color(0xFF4CAF50),
                    icon = Icons.Filled.SmartToy,
                    onPressed = { },
                    title = "Iniciar"
                )
            } else {
                Action(
                    color = Color.Red,
                    icon = Icons.Filled.SmartToy,
                    onPressed = { },
                    title = "Parar"
                )
            }


            Action(
                color = Color(0xFFFFA500),
                icon = Icons.Filled.NextPlan,
                onPressed = { },
                title = "Pular próximo"
            )
        }
    }
}

@Composable
private fun Action(
    color: Color,
    enabled: Boolean = true,
    icon: ImageVector,
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
            Text(title, modifier = Modifier.weight(1f))
        }
    }
}
