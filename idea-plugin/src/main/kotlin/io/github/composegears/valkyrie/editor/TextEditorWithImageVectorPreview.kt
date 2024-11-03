package io.github.composegears.valkyrie.editor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.ui.JBUI
import io.github.composegears.valkyrie.editor.ui.VirtualFileImageVector
import io.github.composegears.valkyrie.ui.foundation.theme.ValkyrieTheme
import java.awt.Dimension
import javax.swing.JComponent

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
                VirtualFileImageVector(
                    modifier = Modifier.fillMaxSize(),
                    file = file,
                )
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
