package app.jotape.ponto_automatico.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import app.jotape.ponto_automatico.titleBarHeight

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun WindowScope.TitleBar(
    title: String,
    centerTitle: Boolean = false,
    onExit: (() -> Unit)? = null,
    onMinimizeRequest: (() -> Unit)? = null
) = WindowDraggableArea {
    var isHoverExit by remember { mutableStateOf(false) }
    var isHoverMinimize by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .height(titleBarHeight.dp)
            .background(Color.DarkGray)
    ) {
        Box {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                textAlign = if (centerTitle) TextAlign.Center else TextAlign.Start,
                color = Color.White,
                modifier =
                Modifier
                    .padding(start = if (centerTitle) 0.dp else 16.dp)
                    .fillMaxWidth()
            )
        }
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (onMinimizeRequest != null)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .onPointerEvent(PointerEventType.Enter) { isHoverMinimize = true }
                        .onPointerEvent(PointerEventType.Exit) { isHoverMinimize = false }
                        .onClick { onMinimizeRequest() }
                        .background(if (isHoverMinimize) Color.LightGray else Color.Transparent)
                        .width(30.dp)
                        .fillMaxHeight()
                ) {
                    Icon(
                        Icons.Default.Minimize,
                        contentDescription = "Minimizar",
                        tint = Color.White
                    )
                }
            if (onExit != null)
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .onPointerEvent(PointerEventType.Enter) { isHoverExit = true }
                        .onPointerEvent(PointerEventType.Exit) { isHoverExit = false }
                        .onClick { onExit() }
                        .background(if (isHoverExit) Color.Red else Color.Transparent)
                        .width(30.dp)
                        .fillMaxHeight()
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = Color.White
                    )
                }
        }
    }
}

