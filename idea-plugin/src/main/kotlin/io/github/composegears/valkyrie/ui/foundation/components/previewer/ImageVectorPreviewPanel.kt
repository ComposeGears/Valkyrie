package io.github.composegears.valkyrie.ui.foundation.components.previewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.compose.toComposeImageVector
import io.github.composegears.valkyrie.ir.util.ColorClassification
import io.github.composegears.valkyrie.ir.util.DominantShade
import io.github.composegears.valkyrie.ir.util.iconColors
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.previewbg.BgType
import io.github.composegears.valkyrie.ui.foundation.previewbg.PreviewBackground
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.IR_STUB
import kotlin.math.min
import kotlinx.coroutines.launch

private sealed interface PanelType {
    data object Initial : PanelType
    data object Error : PanelType
    data class Success(val imageVector: ImageVector) : PanelType
}

@Composable
fun ImageVectorPreviewPanel(
    irImageVector: IrImageVector?,
    previewType: PreviewType,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val zoomState = rememberZoomState()

    val initialWidth by rememberMutableState(irImageVector) { irImageVector?.defaultWidth?.dp ?: Dp.Unspecified }
    val initialHeight by rememberMutableState(irImageVector) { irImageVector?.defaultHeight?.dp ?: Dp.Unspecified }

    var panelType by rememberMutableState<PanelType> { PanelType.Initial }

    var bgType by rememberMutableState(irImageVector, previewType) {
        when (previewType) {
            PreviewType.Black -> BgType.Black
            PreviewType.White -> BgType.White
            PreviewType.Pixel -> BgType.PixelGrid
            PreviewType.Auto -> when (ColorClassification.from(irImageVector?.iconColors().orEmpty())) {
                DominantShade.Black -> BgType.White
                DominantShade.White -> BgType.Black
                DominantShade.Mixed -> BgType.PixelGrid
            }
        }
    }

    LaunchedEffect(irImageVector, zoomState.maxPreviewSize) {
        val maxPreviewSize = zoomState.maxPreviewSize
        if (irImageVector == null || maxPreviewSize == Dp.Unspecified) return@LaunchedEffect

        val initialScale = maxPreviewSize / (3 * min(irImageVector.defaultWidth.dp, irImageVector.defaultHeight.dp))

        zoomState.setScale(initialScale)
    }

    LaunchedEffect(irImageVector, zoomState.scale) {
        panelType = if (irImageVector == null || initialWidth == Dp.Unspecified || initialHeight == Dp.Unspecified) {
            PanelType.Error
        } else {
            PanelType.Success(
                imageVector = irImageVector.toComposeImageVector(
                    defaultWidth = (initialWidth.value * zoomState.scale).dp,
                    defaultHeight = (initialHeight.value * zoomState.scale).dp,
                ),
            )
        }
    }

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        zoomState.maxPreviewSize = min(maxWidth, maxHeight)

        when (val state = panelType) {
            is PanelType.Error -> PreviewParsingError()
            is PanelType.Success -> ImageVectorPreviewUi(
                imageVector = state.imageVector,
                bgType = bgType,
                defaultWidth = initialWidth.value,
                defaultHeight = initialHeight.value,
                zoomIn = {
                    if (zoomState.maxPreviewSize >= min(
                            state.imageVector.defaultWidth,
                            state.imageVector.defaultHeight,
                        )
                    ) {
                        scope.launch {
                            zoomState.zoomIn()
                        }
                    }
                },
                zoomOut = {
                    scope.launch {
                        zoomState.zoomOut()
                    }
                },
                reset = {
                    scope.launch {
                        zoomState.reset()
                    }
                },
                fitToWindow = {
                    scope.launch {
                        with(density) {
                            val scaleFactor = min(
                                zoomState.maxPreviewSize.toPx() / initialWidth.toPx(),
                                zoomState.maxPreviewSize.toPx() / initialWidth.toPx(),
                            )
                            zoomState.animateToScale(scaleFactor)
                        }
                    }
                },
                onBgChange = {
                    bgType = it
                },
            )
            else -> {}
        }
    }
}

@Composable
private fun ImageVectorPreviewUi(
    imageVector: ImageVector,
    bgType: BgType,
    defaultWidth: Float,
    defaultHeight: Float,
    zoomIn: () -> Unit,
    zoomOut: () -> Unit,
    reset: () -> Unit,
    onBgChange: (BgType) -> Unit,
    modifier: Modifier = Modifier,
    fitToWindow: () -> Unit,
) {
    Column(
        modifier = modifier.padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        TopActions(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 12.dp),
            defaultWidth = defaultWidth,
            defaultHeight = defaultHeight,
            onBgChange = onBgChange,
            zoomIn = zoomIn,
            zoomOut = zoomOut,
            onActualSize = reset,
            fitToWindow = fitToWindow,
        )
        VerticalSpacer(8.dp)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterHorizontally),
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
    }
}

@Preview
@Composable
private fun ImageVectorPreviewPanelPreview() = PreviewTheme {
    ImageVectorPreviewPanel(
        modifier = Modifier.fillMaxSize(),
        irImageVector = IR_STUB,
        previewType = PreviewType.Auto,
    )
}
