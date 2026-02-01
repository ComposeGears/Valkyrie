package io.github.composegears.valkyrie.ui.screen.settings.tabs.preview.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.util.stringResource
import kotlin.math.abs
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text

@Composable
fun PreviewBgSection(
    previewType: PreviewType,
    modifier: Modifier = Modifier,
    onSelect: (PreviewType) -> Unit,
) {
    Group(
        modifier = modifier,
        text = stringResource("settings.imagevector.preview.background.header"),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SimplePreviewItem(
                label = stringResource("settings.imagevector.preview.background.black"),
                textColor = Color.White,
                bgType = BgType.Black,
                selected = previewType == PreviewType.Black,
                onSelect = { onSelect(PreviewType.Black) },
            )
            SimplePreviewItem(
                label = stringResource("settings.imagevector.preview.background.white"),
                textColor = Color.Black,
                bgType = BgType.White,
                selected = previewType == PreviewType.White,
                onSelect = { onSelect(PreviewType.White) },
            )
            SimplePreviewItem(
                label = stringResource("settings.imagevector.preview.background.pixel"),
                textColor = Color.Black,
                bgType = BgType.PixelGrid,
                selected = previewType == PreviewType.Pixel,
                onSelect = { onSelect(PreviewType.Pixel) },
            )
            AutoItem(
                label = stringResource("settings.imagevector.preview.background.auto"),
                selected = previewType == PreviewType.Auto,
                onSelect = { onSelect(PreviewType.Auto) },
            )
        }
    }
}

@Composable
private fun SimplePreviewItem(
    label: String,
    textColor: Color,
    bgType: BgType,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    PreviewItem(
        selected = selected,
        onSelect = onSelect,
    ) {
        PreviewBackground(
            modifier = Modifier.matchParentSize(),
            bgType = bgType,
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            color = textColor,
            text = label,
        )
    }
}

@Composable
private fun AutoItem(
    label: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    PreviewItem(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
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
        selected = selected,
        onSelect = onSelect,
        content = {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 4.dp)
                    .blendMode(blendMode = BlendMode.Difference),
                text = label,
            )
        },
    )
}

@Composable
private fun PreviewItem(
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    content: @Composable BoxScope.() -> Unit,
) {
    val color by animateColorAsState(
        when {
            selected -> JewelTheme.primaryColor
            else -> Color.Transparent
        },
    )

    Box(
        modifier = modifier
            .size(72.dp)
            .border(
                width = 2.dp,
                color = color,
                shape = shape,
            )
            .clip(shape = shape)
            .clickable(onClick = onSelect),
        content = content,
    )
}

private fun Modifier.blendMode(blendMode: BlendMode): Modifier {
    return this.drawWithCache {
        val graphicsLayer = obtainGraphicsLayer()
        graphicsLayer.apply {
            record {
                drawContent()
            }
            this.blendMode = blendMode
        }
        onDrawWithContent {
            drawLayer(graphicsLayer)
        }
    }
}

@Preview
@Composable
private fun PreviewBgSectionPreview() = PreviewTheme {
    var previewType by rememberMutableState { PreviewType.Auto }

    PreviewBgSection(
        previewType = previewType,
        onSelect = { previewType = it },
    )
}
