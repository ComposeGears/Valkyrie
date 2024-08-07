package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun PixelGrid(
    modifier: Modifier = Modifier,
    gridSize: Dp = 8.dp,
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val squareSize = gridSize.toPx()

        val verticalSquares = (canvasHeight / squareSize).toInt()
        val horizontalSquares = (canvasWidth / squareSize).toInt()

        repeat(verticalSquares) { j ->
            repeat(horizontalSquares) { i ->
                val color = if ((i + j) % 2 == 0) Color.LightGray else Color.White

                drawRect(
                    color = color,
                    topLeft = Offset(x = i * squareSize, y = j * squareSize),
                    size = Size(width = squareSize, height = squareSize),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PixelGridPreview() = PreviewTheme {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val gridSizes = listOf(8.dp, 4.dp, 2.dp)

        gridSizes.forEach { gridSize ->
            PixelGrid(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(200.dp),
                gridSize = gridSize,
            )
        }
    }
}
