package io.github.composegears.valkyrie.ui.foundation.components.previewer

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.ActualZoomAction
import io.github.composegears.valkyrie.jewel.BlackCircleAction
import io.github.composegears.valkyrie.jewel.ChessboardAction
import io.github.composegears.valkyrie.jewel.FitContentAction
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.VerticalDivider
import io.github.composegears.valkyrie.jewel.WhiteCircleAction
import io.github.composegears.valkyrie.jewel.ZoomInAction
import io.github.composegears.valkyrie.jewel.ZoomOutAction
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun PreviewerToolbar(
    defaultWidth: Float,
    defaultHeight: Float,
    onBgChange: (BgType) -> Unit,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    onActualZoom: () -> Unit,
    onFitToWindow: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Toolbar(modifier = modifier) {
        ChessboardAction(onClick = { onBgChange(BgType.PixelGrid) })
        WhiteCircleAction(onClick = { onBgChange(BgType.White) })
        BlackCircleAction(onClick = { onBgChange(BgType.Black) })
        VerticalDivider(
            modifier = Modifier
                .height(20.dp)
                .padding(horizontal = 2.dp),
        )
        ZoomInAction(onClick = zoomIn)
        ZoomOutAction(onClick = zoomOut)
        ActualZoomAction(onClick = onActualZoom)
        FitContentAction(onClick = onFitToWindow)
        WeightSpacer()
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = "${defaultWidth.toInt()}x${defaultHeight.toInt()}",
            textAlign = TextAlign.End,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = JewelTheme.typography.medium,
        )
    }
}

@Preview
@Composable
private fun PreviewerToolbarPreview() = PreviewTheme {
    PreviewerToolbar(
        defaultWidth = 100f,
        defaultHeight = 100f,
        onBgChange = {},
        zoomIn = {},
        zoomOut = {},
        onActualZoom = {},
        onFitToWindow = {},
    )
}
