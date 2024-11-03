package io.github.composegears.valkyrie.ui.foundation.components.previewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.TooltipIconButton
import io.github.composegears.valkyrie.ui.foundation.TooltipImageButton
import io.github.composegears.valkyrie.ui.foundation.disabled
import io.github.composegears.valkyrie.ui.foundation.icons.ActualZoom
import io.github.composegears.valkyrie.ui.foundation.icons.BlackCircle
import io.github.composegears.valkyrie.ui.foundation.icons.Chessboard
import io.github.composegears.valkyrie.ui.foundation.icons.FitContent
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.WhiteCircle
import io.github.composegears.valkyrie.ui.foundation.icons.ZoomIn
import io.github.composegears.valkyrie.ui.foundation.icons.ZoomOut
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TopActions(
    defaultWidth: Float,
    defaultHeight: Float,
    onBgTypeChange: (BgType) -> Unit,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    onActualSize: () -> Unit,
    fitToWindow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        TooltipIconButton(
            tooltipText = "Show pixel grid",
            icon = ValkyrieIcons.Chessboard,
            onClick = { onBgTypeChange(BgType.PixelGrid) },
        )
        TooltipImageButton(
            tooltipText = "Show white background",
            icon = ValkyrieIcons.WhiteCircle,
            onClick = { onBgTypeChange(BgType.White) },
        )
        TooltipImageButton(
            tooltipText = "Show black background",
            icon = ValkyrieIcons.BlackCircle,
            onClick = { onBgTypeChange(BgType.Black) },
        )
        VerticalDivider(
            modifier = Modifier.height(20.dp).padding(horizontal = 2.dp),
            color = MaterialTheme.colorScheme.onSurface.disabled(),
        )
        TooltipIconButton(
            tooltipText = "Zoom In",
            icon = ValkyrieIcons.ZoomIn,
            onClick = zoomIn,
        )
        TooltipIconButton(
            tooltipText = "Zoom Out",
            icon = ValkyrieIcons.ZoomOut,
            onClick = zoomOut,
        )
        TooltipIconButton(
            tooltipText = "Actual Size",
            icon = ValkyrieIcons.ActualZoom,
            onClick = onActualSize,
        )
        TooltipIconButton(
            tooltipText = "Fit Zoom to Window",
            icon = ValkyrieIcons.FitContent,
            onClick = fitToWindow,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "${defaultWidth.toInt()}x${defaultHeight.toInt()}",
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun TopActionsPreview() = PreviewTheme {
    TopActions(
        defaultWidth = 100f,
        defaultHeight = 100f,
        onBgTypeChange = {},
        modifier = Modifier.padding(8.dp),
        zoomIn = {},
        zoomOut = {},
        onActualSize = {},
        fitToWindow = {},
    )
}
