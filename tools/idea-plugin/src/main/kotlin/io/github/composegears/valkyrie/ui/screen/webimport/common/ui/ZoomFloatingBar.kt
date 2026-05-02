package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.ActualZoomAction
import io.github.composegears.valkyrie.jewel.VerticalDivider
import io.github.composegears.valkyrie.jewel.ZoomInAction
import io.github.composegears.valkyrie.jewel.ZoomOutAction
import io.github.composegears.valkyrie.jewel.button.TooltipIconButton
import io.github.composegears.valkyrie.jewel.colors.onOverlay
import io.github.composegears.valkyrie.jewel.colors.overlay
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography

/** Default scale factor (100%). */
const val ZOOM_DEFAULT_SCALE = 1f

/** Minimum scale factor (25%). */
private const val ZOOM_MIN_SCALE = 0.25f

/** Maximum scale factor (200%). */
private const val ZOOM_MAX_SCALE = 2.0f

/** Scale step for each zoom in/out action (~25%). */
private const val ZOOM_SCALE_STEP = 0.25f

private const val FIGURE_SPACE = '\u2007'

/** Formats an integer zoom percent as a fixed-width label, padding 2-digit values with a figure space. */
private fun Int.toZoomLabel(): String = toString().padStart(3, FIGURE_SPACE) + "%"

private fun Float.zoomIn() = (this + ZOOM_SCALE_STEP).coerceAtMost(ZOOM_MAX_SCALE)
private fun Float.zoomOut() = (this - ZOOM_SCALE_STEP).coerceAtLeast(ZOOM_MIN_SCALE)

/**
 * Floating expandable zoom control bar for changing the visual display size of icons.
 *
 * This does NOT affect any import settings — it only controls how large icons appear
 * in the grid. In collapsed state shows only the current zoom percentage; clicking it
 * expands the bar. In expanded state shows zoom-out, zoom-in, reset controls, the
 * percentage label, and a chevron-right to collapse.
 *
 * @param scaleFactor Current scale factor in [ZOOM_MIN_SCALE]..[ZOOM_MAX_SCALE], default [ZOOM_DEFAULT_SCALE].
 * @param onScaleChange Callback invoked with the new scale factor when the user changes the zoom level.
 * @param modifier Modifier to apply to the bar.
 */
@Composable
fun ZoomFloatingBar(
    scaleFactor: Float,
    onScaleChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberMutableState { false }

    val zoomPercent = (scaleFactor * 100).toInt()
    val canZoomIn = scaleFactor < ZOOM_MAX_SCALE
    val canZoomOut = scaleFactor > ZOOM_MIN_SCALE

    Row(
        modifier = modifier
            .heightIn(min = 32.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(20))
            .background(JewelTheme.overlay)
            .clickable(
                enabled = !expanded,
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { expanded = true },
            )
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ZoomOutAction(
                    onClick = { onScaleChange(scaleFactor.zoomOut()) },
                    enabled = canZoomOut,
                )
                ZoomInAction(
                    onClick = { onScaleChange(scaleFactor.zoomIn()) },
                    enabled = canZoomIn,
                )
                ActualZoomAction(
                    enabled = scaleFactor != ZOOM_DEFAULT_SCALE,
                    onClick = { onScaleChange(ZOOM_DEFAULT_SCALE) },
                )
                VerticalDivider(
                    modifier = Modifier.height(16.dp),
                    color = JewelTheme.onOverlay,
                )
            }
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            Spacer(4.dp)
        }
        AnimatedVisibility(
            visible = !expanded,
            enter = expandHorizontally(expandFrom = Alignment.Start) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.Start) + fadeOut(),
        ) {
            Icon(
                key = AllIconsKeys.General.ChevronLeft,
                contentDescription = null,
            )
        }
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = if (expanded) zoomPercent.toZoomLabel() else "$zoomPercent%",
            style = JewelTheme.typography.small.copy(fontFeatureSettings = "tnum"),
        )
        AnimatedVisibility(
            visible = expanded,
            enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
            exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut(),
        ) {
            TooltipIconButton(
                key = AllIconsKeys.General.ChevronRight,
                contentDescription = null,
                tooltipText = stringResource("component.toolbar.collapse.tooltip"),
                onClick = { expanded = !expanded },
            )
        }
    }
}

@Preview
@Composable
private fun ZoomFloatingBarCollapsedPreview() = PreviewTheme(alignment = Alignment.CenterEnd) {
    var scale by rememberMutableState { ZOOM_DEFAULT_SCALE }

    ZoomFloatingBar(
        modifier = Modifier.padding(end = 16.dp),
        scaleFactor = scale,
        onScaleChange = { scale = it },
    )
}
