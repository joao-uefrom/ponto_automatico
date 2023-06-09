package app.jotape.ui.home.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.jotape.models.Log
import app.jotape.services.LogsService
import app.jotape.ui.home.HomeUIState

private const val weightEvent = .2f
private const val weightMessage = .5f
private const val weightDate = .3f
private const val rowHeight = 24
private const val rowHorizontalPadding = 4
private const val rowFontSize = 12

@Composable
fun LogTable(uiState: HomeUIState, modifier: Modifier) {
    val logs by LogsService.logs.collectAsState()
    val scrollState = rememberScrollState(0)

    Column(modifier) {
        SectionTitle("Logs", Modifier.padding(top = 8.dp))

        Column {
            LogHead()
            Divider()

            Box(Modifier.fillMaxSize()) {
                Column(Modifier.verticalScroll(scrollState).fillMaxSize()) {
                    logs.forEach { log ->
                        LogCell(log)
                        Divider()
                    }
                }

                VerticalScrollbar(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    adapter = rememberScrollbarAdapter(scrollState)
                )
            }

        }
    }
}

@Composable
private fun LogHead() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.background(color = Color(0xFF1F1F1F))
    ) {
        Text(
            "Classe",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(weightEvent).padding(start = rowHorizontalPadding.dp)
        )
        Divider(Modifier.width(1.dp).height(rowHeight.dp))
        Text(
            "Mensagem",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(weightMessage).padding(start = rowHorizontalPadding.dp)
        )
        Divider(Modifier.width(1.dp).height(rowHeight.dp))
        Text(
            "Data",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(weightDate).padding(start = rowHorizontalPadding.dp)
        )
    }
}

@Composable
private fun LogCell(log: Log) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Tooltip(
            log.`class`,
            Modifier.weight(weightEvent).padding(horizontal = rowHorizontalPadding.dp)
        ) {
            Text(text = log.`class`, fontSize = rowFontSize.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Divider(Modifier.width(1.dp).height(rowHeight.dp))
        Tooltip(
            log.message,
            Modifier.weight(weightMessage).padding(horizontal = rowHorizontalPadding.dp)
        ) {
            Text(text = log.message, fontSize = rowFontSize.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Divider(Modifier.width(1.dp).height(rowHeight.dp))
        Tooltip(
            log.createdAt.toString(),
            Modifier.weight(weightDate).padding(horizontal = rowHorizontalPadding.dp)
        ) {
            Text(text = log.createdAt.toString(), fontSize = rowFontSize.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Tooltip(description: String, modifier: Modifier, content: @Composable () -> Unit) {
    TooltipArea(
        tooltip = {
            Surface(
                modifier = Modifier.shadow(4.dp),
                color = Color(255, 255, 210),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = description,
                    color = Color.Black,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        modifier = modifier,
        delayMillis = 600
    ) {
        content()
    }
}