package app.jotape.ui.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import app.jotape.ui.home.HomeViewModel
import app.jotape.ui.home.HomeUIState

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SchedulesForm(uiState: HomeUIState, modifier: Modifier) {
    Column(modifier) {
        SectionTitle("Horários")

        Column(
            Modifier.padding(horizontal = 16.dp)
        ) {
            Row {
                TextField(
                    label = { Text("Horário") },
                    leadingIcon = { Icon(Icons.Filled.Schedule, "") },
                    modifier = Modifier.weight(1f)
                        .onPreviewKeyEvent {
                            when {
                                (it.key == Key.Enter) -> {
                                    HomeViewModel.addSchedule(uiState.schedule)
                                    true
                                }

                                else -> false
                            }
                        },
                    onValueChange = HomeViewModel::setSchedule,
                    singleLine = true,
                    value = uiState.schedule,
                )

                Button(
                    onClick = { HomeViewModel.addSchedule(uiState.schedule) },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.CenterVertically)
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            SchedulesList(uiState.schedules, HomeViewModel::removeSchedule)
        }
    }
}
