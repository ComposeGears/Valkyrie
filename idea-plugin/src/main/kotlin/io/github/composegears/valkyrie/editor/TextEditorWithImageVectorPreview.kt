package io.github.composegears.valkyrie.editor

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFileFactory
import com.intellij.util.ui.JBUI
import io.github.composegears.valkyrie.psi.imagevector.ImageVectorPsiParser
import io.github.composegears.valkyrie.ui.foundation.PixelGrid
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme
import java.awt.Dimension
import javax.swing.JComponent
import org.jetbrains.kotlin.idea.KotlinFileType
import org.jetbrains.kotlin.psi.KtFile

class TextEditorWithImageVectorPreview(
    project: Project,
    file: VirtualFile,
) : TextEditorWithPreview(
    /* editor = */
    createTextEditor(project, file),
    /* preview = */
    createPreviewEditor(project, file),
) {
    companion object {
        private fun createTextEditor(project: Project, file: VirtualFile): TextEditor {
            return TextEditorProvider.getInstance().createEditor(project, file) as TextEditor
        }

        private fun createPreviewEditor(project: Project, file: VirtualFile): FileEditor {
            return ImageVectorPreviewEditor(project, file)
        }
    }
}

private class ImageVectorPreviewEditor(
    private val project: Project,
    private val file: VirtualFile,
) : FileEditor {

    init {
        System.setProperty("compose.swing.render.on.graphics", "true")
        System.setProperty("compose.interop.blending", "true")
    }

    private val composePanel = ComposePanel().apply {
        setContent {
            ValkyrieTheme(project, this) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        val imageVector = remember(file) {
                            val ktFile = file.toKtFile(project)
                            if (ktFile == null) {
                                null
                            } else {
                                ImageVectorPsiParser.parseToImageVector(ktFile)
                            }
                        }

                        if (imageVector == null) {
                            Text("Failed to parse to ImageVector, please submit issue")
                        } else {
                            Box(modifier = Modifier.size(200.dp)) {
                                PixelGrid(
                                    modifier = Modifier.matchParentSize(),
                                    gridSize = 2.dp,
                                )
                                Image(
                                    modifier = Modifier.size(200.dp),
                                    imageVector = imageVector,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }
        }
        preferredSize = JBUI.size(Dimension(800, 800))
    }

    override fun getComponent(): JComponent = composePanel

    override fun getPreferredFocusedComponent(): JComponent = composePanel

    override fun getName(): String = "Kotlin File Preview"

    override fun setState(state: FileEditorState) {}

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: java.beans.PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: java.beans.PropertyChangeListener) {}

    override fun <T : Any?> getUserData(p0: Key<T>): T? = null

    override fun <T : Any?> putUserData(p0: Key<T>, p1: T?) = Unit

    override fun dispose() {}
}

private fun VirtualFile.toKtFile(project: Project): KtFile? {
    val fileContent = contentsToByteArray().toString(Charsets.UTF_8)

    return PsiFileFactory.getInstance(project)
        .createFileFromText(name, KotlinFileType.INSTANCE, fileContent) as? KtFile
}
