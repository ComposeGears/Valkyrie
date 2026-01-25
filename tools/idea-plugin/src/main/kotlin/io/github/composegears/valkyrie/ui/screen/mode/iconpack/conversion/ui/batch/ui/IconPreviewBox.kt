package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.util.dominantShadeColor
import io.github.composegears.valkyrie.sdk.ir.util.internal.DominantShade
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.util.IR_STUB
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun IconPreviewBox(
    irImageVector: IrImageVector,
    previewType: PreviewType,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
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
            .size(size)
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
            gridSize = (size.value / 11f).dp,
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
private fun IconPreviewBoxPreview() = PreviewTheme(alignment = Alignment.Center) {
    var previewType by rememberMutableState { PreviewType.Pixel }

    CenterVerticalRow {
        IconPreviewBox(
            irImageVector = IR_STUB,
            previewType = previewType,
        )
        Spacer(8.dp)
        IconPreviewBox(
            irImageVector = IR_STUB,
            previewType = previewType,
            size = 48.dp,
        )
    }
}
