package io.github.composegears.valkyrie.ui.foundation.previewbg

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.PixelGrid
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun PreviewBackground(
    bgType: BgType,
    modifier: Modifier = Modifier,
    gridSize: Dp = 8.dp,
) {
    Box(modifier = modifier) {
        when (bgType) {
            BgType.Black -> {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black),
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
                    gridSize = gridSize,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewBackgroundPreview() = PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        PreviewBackground(
            modifier = Modifier.size(200.dp),
            bgType = BgType.PixelGrid,
        )
        PreviewBackground(
            modifier = Modifier.size(200.dp),
            bgType = BgType.Black,
        )
        PreviewBackground(
            modifier = Modifier.size(200.dp),
            bgType = BgType.White,
        )
    }
}
