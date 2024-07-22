package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.PixelGrid
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun IconPreviewBox(painter: Painter) {
    var bgType by rememberMutableState { BgType.PixelGrid }

    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        bgType = bgType.next()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        when (bgType) {
            BgType.Black -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black)
                )
            }
            BgType.White -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.White),
                )
            }
            BgType.PixelGrid -> {
                PixelGrid(
                    modifier = Modifier.matchParentSize(),
                    gridSize = 2.dp
                )
            }
        }
        Image(
            modifier = Modifier.size(36.dp),
            painter = painter,
            contentDescription = null
        )
    }
}

private enum class BgType {
    Black,
    White,
    PixelGrid;

    fun next(): BgType = when (this) {
        Black -> White
        White -> PixelGrid
        PixelGrid -> Black
    }
}

@Preview
@Composable
private fun IconPreviewBoxPreview() = PreviewTheme {
    Box(modifier = Modifier.fillMaxSize()) {
        IconPreviewBox(painter = painterResource("META-INF/pluginIcon.svg"))
    }
}