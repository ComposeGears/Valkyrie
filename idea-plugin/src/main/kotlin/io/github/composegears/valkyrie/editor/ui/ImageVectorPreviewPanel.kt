@file:Suppress("NAME_SHADOWING")

package io.github.composegears.valkyrie.editor.ui

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
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
import com.intellij.openapi.application.readAction
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.editor.toKtFile
import io.github.composegears.valkyrie.editor.ui.error.PreviewParsingError
import io.github.composegears.valkyrie.editor.ui.previewer.ImageVectorPreview
import io.github.composegears.valkyrie.editor.ui.previewer.rememberZoomState
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.parser.ktfile.util.toComposeImageVector
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import kotlin.math.min
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.psi.KtFile

sealed interface PanelState {
    data object Initial : PanelState
    data object Error : PanelState
    data class Success(val imageVector: ImageVector) : PanelState
}

@Composable
fun ImageVectorPreviewPanel(
    file: VirtualFile,
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val project = LocalProject.current

    val scope = rememberCoroutineScope()
    val zoomState = rememberZoomState()

    var panelState by rememberMutableState<PanelState> { PanelState.Initial }

    var initialViewportWidth by rememberMutableState { Dp.Unspecified }
    var initialViewportHeight by rememberMutableState { Dp.Unspecified }

    val ktFile by produceState<KtFile?>(null) {
        value = readAction {
            file.toKtFile(project)
        }
    }
    var irImageVector by rememberMutableState<IrImageVector?> { null }

    LaunchedEffect(ktFile) {
        val ktFile = ktFile ?: return@LaunchedEffect

        readAction {
            irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)?.also {
                initialViewportWidth = it.defaultWidth.dp
                initialViewportHeight = it.defaultHeight.dp

                val maxPreviewSize = zoomState.maxPreviewSize
                val initialScale = maxPreviewSize / (3 * min(initialViewportWidth, initialViewportHeight))

                launch {
                    zoomState.setScale(initialScale)
                }
            }
        }

        if (irImageVector == null) {
            panelState = PanelState.Error
        }
    }

    LaunchedEffect(irImageVector, zoomState.scale) {
        val irImageVector = irImageVector ?: return@LaunchedEffect

        panelState = PanelState.Success(
            imageVector = irImageVector.toComposeImageVector(
                defaultWidth = (initialViewportWidth.value * zoomState.scale).dp,
                defaultHeight = (initialViewportHeight.value * zoomState.scale).dp,
            ),
        )
    }

    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        zoomState.maxPreviewSize = min(maxWidth, maxHeight)

        when (val state = panelState) {
            is PanelState.Error -> PreviewParsingError()
            is PanelState.Success -> ImageVectorPreview(
                imageVector = state.imageVector,
                defaultWidth = initialViewportWidth.value,
                defaultHeight = initialViewportHeight.value,
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
                                zoomState.maxPreviewSize.toPx() / initialViewportWidth.toPx(),
                                zoomState.maxPreviewSize.toPx() / initialViewportHeight.toPx(),
                            )
                            zoomState.animateToScale(scaleFactor)
                        }
                    }
                },
            )
            else -> {}
        }
    }
}
