package io.github.composegears.valkyrie.editor.ui.previewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType.PixelGrid
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun ImageVectorPreview(
    imageVector: ImageVector,
    defaultWidth: Float,
    defaultHeight: Float,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    reset: () -> Unit,
    modifier: Modifier = Modifier,
    fitToWindow: () -> Unit,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        var bgType by rememberMutableState { PixelGrid }

        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Box {
                PreviewBackground(
                    bgType = bgType,
                    modifier = Modifier.matchParentSize(),
                )
                Image(
                    imageVector = imageVector,
                    contentDescription = null,
                )
            }
        }
        TopActions(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.TopStart)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            defaultWidth = defaultWidth,
            defaultHeight = defaultHeight,
            onBgTypeChange = {
                bgType = it
            },
            zoomIn = zoomIn,
            zoomOut = zoomOut,
            onActualSize = reset,
            fitToWindow = fitToWindow,
        )
    }
}

@Preview
@Composable
private fun ImageVectorPreviewPreview() = PreviewTheme {
    ImageVectorPreview(
        imageVector = ImageVector.Builder(
            name = "Outlined.Add",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).apply {
            path(fill = SolidColor(Color(0xFF232F34))) {
                moveTo(19f, 13f)
                lineTo(13f, 13f)
                lineTo(13f, 19f)
                lineTo(11f, 19f)
                lineTo(11f, 13f)
                lineTo(5f, 13f)
                lineTo(5f, 11f)
                lineTo(11f, 11f)
                lineTo(11f, 5f)
                lineTo(13f, 5f)
                lineTo(13f, 11f)
                lineTo(19f, 11f)
                lineTo(19f, 13f)
                close()
            }
        }.build(),
        defaultWidth = 24f,
        defaultHeight = 24f,
        zoomIn = {},
        zoomOut = {},
        reset = {},
        fitToWindow = {},
    )
}
