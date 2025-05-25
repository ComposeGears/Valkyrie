package io.github.composegears.valkyrie.ui.foundation.components.previewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.ui.util.disabled
import io.github.composegears.valkyrie.ui.foundation.TooltipIconButton
import io.github.composegears.valkyrie.ui.foundation.TooltipImageButton
import io.github.composegears.valkyrie.ui.foundation.icons.ActualZoom
import io.github.composegears.valkyrie.ui.foundation.icons.BlackCircle
import io.github.composegears.valkyrie.ui.foundation.icons.Chessboard
import io.github.composegears.valkyrie.ui.foundation.icons.FitContent
import io.github.composegears.valkyrie.ui.foundation.icons.WhiteCircle
import io.github.composegears.valkyrie.ui.foundation.icons.ZoomIn
import io.github.composegears.valkyrie.ui.foundation.icons.ZoomOut
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TopActions(
    defaultWidth: Float,
    defaultHeight: Float,
    onBgChange: (BgType) -> Unit,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    onActualSize: () -> Unit,
    fitToWindow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        TooltipIconButton(
            hint = "Show pixel grid",
            icon = ValkyrieIcons.Chessboard,
            onClick = { onBgChange(BgType.PixelGrid) },
        )
        TooltipImageButton(
            hint = "Show white background",
            image = ValkyrieIcons.WhiteCircle,
            onClick = { onBgChange(BgType.White) },
        )
        TooltipImageButton(
            hint = "Show black background",
            image = ValkyrieIcons.BlackCircle,
            onClick = { onBgChange(BgType.Black) },
        )
        VerticalDivider(
            modifier = Modifier.height(20.dp).padding(horizontal = 2.dp),
            color = MaterialTheme.colorScheme.onSurface.disabled(),
        )
        TooltipIconButton(
            hint = "Zoom In",
            icon = ValkyrieIcons.ZoomIn,
            onClick = zoomIn,
        )
        TooltipIconButton(
            hint = "Zoom Out",
            icon = ValkyrieIcons.ZoomOut,
            onClick = zoomOut,
        )
        TooltipIconButton(
            hint = "Actual Size",
            icon = ValkyrieIcons.ActualZoom,
            onClick = onActualSize,
        )
        TooltipIconButton(
            hint = "Fit Zoom to Window",
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
        onBgChange = {},
        modifier = Modifier.padding(8.dp),
        zoomIn = {},
        zoomOut = {},
        onActualSize = {},
        fitToWindow = {},
    )
}
