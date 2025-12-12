package io.github.composegears.valkyrie.ui.screen.settings.tabs.previewSettings

import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.blendMode
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreviewBgSection(
    previewType: PreviewType,
    modifier: Modifier = Modifier,
    onSelect: (PreviewType) -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "Preview background",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            BlackItem(previewType = previewType, onSelect = onSelect)
            WhiteItem(previewType = previewType, onSelect = onSelect)
            PixelItem(previewType = previewType, onSelect = onSelect)
            AutoItem(previewType = previewType, onSelect = onSelect)
        }
    }
}

@Composable
private fun BlackItem(
    previewType: PreviewType,
    onSelect: (PreviewType) -> Unit,
) {
    PreviewItem(
        selected = previewType == PreviewType.Black,
        onSelect = { onSelect(PreviewType.Black) },
        content = {
            PreviewBackground(
                modifier = Modifier.matchParentSize(),
                bgType = BgType.Black,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                color = Color.White,
                text = "Black",
            )
        },
    )
}

@Composable
private fun WhiteItem(
    previewType: PreviewType,
    onSelect: (PreviewType) -> Unit,
) {
    PreviewItem(
        selected = previewType == PreviewType.White,
        onSelect = { onSelect(PreviewType.White) },
        content = {
            PreviewBackground(
                modifier = Modifier.matchParentSize(),
                bgType = BgType.White,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black,
                text = "White",
            )
        },
    )
}

@Composable
private fun PixelItem(
    previewType: PreviewType,
    onSelect: (PreviewType) -> Unit,
) {
    PreviewItem(
        selected = previewType == PreviewType.Pixel,
        onSelect = { onSelect(PreviewType.Pixel) },
        content = {
            PreviewBackground(
                modifier = Modifier.matchParentSize(),
                bgType = BgType.PixelGrid,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black,
                text = "Pixel",
            )
        },
    )
}

@Composable
private fun AutoItem(
    previewType: PreviewType,
    onSelect: (PreviewType) -> Unit,
) {
    PreviewItem(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .drawBehind {
                drawPath(
                    path = Path().apply {
                        moveTo(0f, 0f)
                        lineTo(0f, size.height)
                        lineTo(size.width / 2f, size.height / 2f)
                        lineTo(size.width / 2f, 0f)
                        close()
                    },
                    color = Color.Black,
                )
                drawPath(
                    path = Path().apply {
                        moveTo(size.width, size.height)
                        lineTo(size.width, 0f)
                        lineTo(size.width / 2f, 0f)
                        lineTo(size.width / 2f, size.height / 2f)
                        close()
                    },
                    color = Color.White,
                )

                clipPath(
                    path = Path().apply {
                        moveTo(0f, size.height)
                        lineTo(size.center.x, size.center.y)
                        lineTo(size.width, size.height)
                        close()
                    },
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val squareSize = 15f
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
            },
        selected = previewType == PreviewType.Auto,
        onSelect = { onSelect(PreviewType.Auto) },
        content = {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 4.dp)
                    .blendMode(blendMode = BlendMode.Difference),
                text = "Auto",
            )
        },
    )
}

@Composable
private fun PreviewItem(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val color by animateColorAsState(
        when {
            selected -> MaterialTheme.colorScheme.primary
            else -> Color.Transparent
        },
    )

    Box(
        modifier = modifier
            .size(72.dp)
            .border(
                width = 2.dp,
                color = color,
                shape = MaterialTheme.shapes.small,
            )
            .clip(shape = MaterialTheme.shapes.small)
            .clickable(onClick = onSelect),
        content = content,
    )
}

@Preview
@Composable
private fun ImageVectorPreviewSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    PreviewBgSection(
        previewType = PreviewType.Auto,
        onSelect = {},
    )
}
