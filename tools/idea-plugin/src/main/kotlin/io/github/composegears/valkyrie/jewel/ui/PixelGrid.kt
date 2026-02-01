package io.github.composegears.valkyrie.jewel.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import kotlin.math.abs
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PixelGrid(
    modifier: Modifier = Modifier,
    gridSize: Dp = 8.dp,
) {
    Canvas(modifier = modifier.clipToBounds()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val squareSize = gridSize.toPx()

        val verticalSquares = (canvasHeight / squareSize).roundToInt()
        val horizontalSquares = (canvasWidth / squareSize).roundToInt()

        drawRect(
            color = Color.White,
            topLeft = Offset(x = 0f, y = 0f),
            size = Size(width = canvasWidth, height = canvasHeight),
        )
        translate(
            left = canvasWidth / 2f + squareSize / 2f,
            top = canvasHeight / 2f + squareSize / 2f,
        ) {
            for (i in -horizontalSquares / 2 - 2..horizontalSquares / 2) {
                for (j in -verticalSquares / 2 - 2..verticalSquares / 2) {
                    if (abs(i % 2) == abs(j % 2)) {
                        drawRect(
                            color = Color.LightGray,
                            topLeft = Offset(x = i * squareSize, y = j * squareSize),
                            size = Size(width = squareSize, height = squareSize),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PixelGridPreview() = PreviewTheme {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        val gridSizes = listOf(64.dp, 32.dp, 13.dp, 8.dp, 4.dp)

        gridSizes.forEach { gridSize ->
            PixelGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                gridSize = gridSize,
            )
        }
    }
}
