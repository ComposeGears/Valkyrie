@file:Suppress("NAME_SHADOWING")

package io.github.composegears.valkyrie.editor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.intellij.openapi.application.readAction
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.editor.toKtFile
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.ui.foundation.components.previewer.ImageVectorPreviewPanel
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import org.jetbrains.kotlin.psi.KtFile

@Composable
fun VirtualFileImageVector(
    file: VirtualFile,
    modifier: Modifier = Modifier,
) {
    val project = LocalProject.current

    var irImageVector by rememberMutableState<IrImageVector?> { null }
    var isPreparing by rememberMutableState { true }

    val ktFile by produceState<KtFile?>(null) {
        value = readAction {
            file.toKtFile(project)
        }
    }

    LaunchedEffect(ktFile) {
        val ktFile = ktFile ?: return@LaunchedEffect

        readAction {
            isPreparing = true
            irImageVector = ImageVectorPsiParser.parseToIrImageVector(ktFile)
            isPreparing = false
        }
    }

    if (!isPreparing) {
        ImageVectorPreviewPanel(
            modifier = modifier.fillMaxSize(),
            irImageVector = irImageVector,
        )
    }
}
