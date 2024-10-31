package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.SizeSpacer
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.PainterConverter
import java.nio.file.Path
import kotlin.io.path.Path
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun IconPreviewBox(
    path: Path,
    modifier: Modifier = Modifier,
) {
    var bgType by rememberMutableState { BgType.PixelGrid }

    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        bgType = bgType.next()
                    },
                )
            },
        contentAlignment = Alignment.Center,
    ) {
        PreviewBackground(
            bgType = bgType,
            gridSize = (56f / 11f).dp,
            modifier = Modifier.matchParentSize(),
        )

        val iconPainter by produceState<Painter?>(initialValue = null) {
            value = withContext(Dispatchers.Default) {
                PainterConverter.from(path = path)
            }
        }

        when (val painter = iconPainter) {
            null -> SizeSpacer(36.dp)
            else -> Image(
                modifier = Modifier.size(36.dp),
                painter = painter,
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
private fun IconPreviewBoxPreview() = PreviewTheme {
    IconPreviewBox(path = Path("META-INF/pluginIcon.svg"))
}
