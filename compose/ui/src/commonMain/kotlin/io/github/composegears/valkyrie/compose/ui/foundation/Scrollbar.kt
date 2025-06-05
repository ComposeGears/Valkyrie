package io.github.composegears.valkyrie.compose.ui.foundation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.collectLatest

@Composable
fun BoxScope.VerticalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
) {
    StyledVerticalScrollbar(
        modifier = modifier
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .padding(end = 4.dp, top = 8.dp, bottom = 4.dp),
        adapter = adapter,
    )
}

@Composable
fun BoxScope.HorizontalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
) {
    StyledVerticalScrollbar(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .padding(start = 8.dp, end = 20.dp, bottom = 4.dp),
        adapter = adapter,
    )
}

@Composable
fun AnimatedVerticalScrollbar(
    adapter: ScrollbarAdapter,
    isScrollInProgress: Boolean,
    modifier: Modifier = Modifier,
) {
    val isScrolling by rememberUpdatedState(isScrollInProgress)
    val interactionSource = remember { MutableInteractionSource() }
    val isDragged by interactionSource.collectIsDraggedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()
    val scrollbarAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        snapshotFlow { isDragged || isHovered || isScrolling }.collectLatest {
            if (it) {
                scrollbarAlpha.animateTo(1f, FadeInAnimationSpec)
            } else {
                scrollbarAlpha.animateTo(0f, FadeOutAnimationSpec)
            }
        }
    }
    StyledVerticalScrollbar(
        adapter = adapter,
        modifier = modifier.graphicsLayer { alpha = scrollbarAlpha.value },
        interactionSource = interactionSource,
    )
}

@Composable
private fun StyledVerticalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val scrollbarStyle = LocalScrollbarStyle.current.copy(
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    )
    VerticalScrollbar(
        adapter = adapter,
        modifier = modifier,
        interactionSource = interactionSource,
        style = scrollbarStyle,
    )
}

private val FadeOutAnimationSpec = tween<Float>(durationMillis = 250, delayMillis = 1500)
private val FadeInAnimationSpec = tween<Float>(durationMillis = 250)
