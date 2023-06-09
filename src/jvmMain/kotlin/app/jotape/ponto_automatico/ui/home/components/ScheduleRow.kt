package app.jotape.ponto_automatico.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.jotape.ponto_automatico.models.Schedule

@Composable
fun ScheduleRow(
    schedule: Schedule,
    onDelete: (schedule: Schedule) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = schedule.hour.toString(),
            modifier = Modifier.padding(vertical = 4.dp)
        )
        Button(
            onClick = { onDelete(schedule) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
            modifier = Modifier.padding(vertical = 8.dp).height(33.dp)
        ) {
            Icon(Icons.Filled.Delete, "Apagar horário $schedule")
        }
    }
}