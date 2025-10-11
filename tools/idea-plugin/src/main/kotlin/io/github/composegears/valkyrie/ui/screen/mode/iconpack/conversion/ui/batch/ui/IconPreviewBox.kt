package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.ir.util.DominantShade
import io.github.composegears.valkyrie.ir.util.dominantShadeColor
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.IR_STUB

@Composable
fun IconPreviewBox(
    irImageVector: IrImageVector,
    previewType: PreviewType,
    modifier: Modifier = Modifier,
) {
    var bgType by rememberSaveable {
        mutableStateOf(
            when (previewType) {
                PreviewType.Black -> BgType.Black
                PreviewType.White -> BgType.White
                PreviewType.Pixel -> BgType.PixelGrid
                PreviewType.Auto -> when (irImageVector.dominantShadeColor) {
                    DominantShade.Black -> BgType.White
                    DominantShade.White -> BgType.Black
                    DominantShade.Mixed -> BgType.PixelGrid
                }
            },
        )
    }

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

        val imageVector = remember { irImageVector.toComposeImageVector() }
        Image(
            modifier = Modifier.size(36.dp),
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

@Preview
@Composable
private fun IconPreviewBoxPreview() = PreviewTheme {
    IconPreviewBox(
        irImageVector = IR_STUB,
        previewType = PreviewType.Auto,
    )
}
