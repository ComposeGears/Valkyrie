package io.github.composegears.valkyrie.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.impl.text.QuickDefinitionProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import io.github.composegears.valkyrie.service.PersistentSettings.Companion.persistentSettings

class ImageVectorPreviewEditorProvider :
    FileEditorProvider,
    QuickDefinitionProvider,
    DumbAware {
    override fun getEditorTypeId(): String = "ImageVectorPreviewEditorProvider"

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun accept(project: Project, file: VirtualFile): Boolean {
        val showImageVectorPreview = project.persistentSettings.state.showImageVectorPreview

        if (file.extension != "kt" || !showImageVectorPreview) return false

        val content = file.inputStream
            .bufferedReader()
            .use { it.readText() }

        if (content.contains("package androidx.compose.material.icons\n")) {
            return false
        }

        return content.contains("androidx.compose.ui.graphics.vector.ImageVector") &&
            (
                content.contains("androidx.compose.ui.graphics.vector.path") ||
                    content.contains("androidx.compose.ui.graphics.vector.group") ||
                    content.contains("androidx.compose.material.icons.materialIcon") ||
                    content.contains("androidx.compose.material.icons.materialPath")
                )
    }

    override fun acceptRequiresReadAction(): Boolean = false

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        return TextEditorWithImageVectorPreview(project, file)
    }
}
