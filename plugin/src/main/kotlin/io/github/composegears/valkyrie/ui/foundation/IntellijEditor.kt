package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.ui.EditorTextFieldProvider
import com.intellij.ui.SimpleEditorCustomization
import io.github.composegears.valkyrie.theme.LocalProject

@Composable
fun IntellijEditorTextField(
    text: String,
    modifier: Modifier = Modifier,
) {
    val project = LocalProject.current
    val document = remember(text) { EditorFactory.getInstance().createDocument(text) }
    val language = remember {
        (FileTypeManager.getInstance().getStdFileType("Kotlin") as LanguageFileType).language
    }
    SwingPanel(
        modifier = modifier,
        factory = {
            EditorTextFieldProvider.getInstance().getEditorField(
                /* language = */ language,
                /* project = */ project,
                /* features = */ listOf(EditorCustomization(true))
            ).apply {
                setDocument(document)
            }
        },
    )
}

private class EditorCustomization(enabled: Boolean) : SimpleEditorCustomization(enabled) {

    override fun customize(editor: EditorEx) {
        editor.settings.isLineNumbersShown = true
        editor.setHorizontalScrollbarVisible(false)
        editor.setVerticalScrollbarVisible(true)
        editor.setBorder(null)
    }
}