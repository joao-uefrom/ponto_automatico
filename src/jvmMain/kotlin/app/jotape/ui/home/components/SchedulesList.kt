package app.jotape.ui.home.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.jotape.models.Schedule

@Composable
fun SchedulesList(
    schedules: List<Schedule>,
    onDelete: (schedule: Schedule) -> Unit
) {
    if (schedules.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Nenhum horário agendado")
        }
    } else {
        val scrollState = rememberScrollState(0)
        val adapter = rememberScrollbarAdapter(scrollState)
        val endPadding = if (adapter.contentSize != adapter.viewportSize) 14.dp else 0.dp

        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .verticalScroll(scrollState)
                    .padding(end = endPadding)
            ) {
                schedules.forEachIndexed { i, schedule ->
                    Column {
                        ScheduleRow(schedule = schedule, onDelete = onDelete)
                        if (i != schedules.lastIndex) Divider()
                    }
                }
            }

            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd),
                adapter = adapter
            )
        }
    }
}