package io.github.composegears.valkyrie.ui.platform

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.platform.LocalInspectionMode
import com.intellij.lang.Language
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.ui.EditorTextFieldProvider
import com.intellij.ui.SimpleEditorCustomization
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import javax.swing.ScrollPaneConstants

@Composable
fun IntellijEditorTextField(
    text: String,
    modifier: Modifier = Modifier,
) {
    if (LocalInspectionMode.current) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "EditorTextField")
        }
    } else {
        val project = LocalProject.current
        val document = remember(text) { EditorFactory.getInstance().createDocument(text) }

        SwingPanel(
            modifier = modifier,
            factory = {
                EditorTextFieldProvider.getInstance().getEditorField(
                    /* language = */
                    kotlinLanguage,
                    /* project = */
                    project,
                    /* features = */
                    listOf(EditorCustomization(true)),
                ).apply {
                    setDocument(document)
                }
            },
        )
    }
}

private class EditorCustomization(enabled: Boolean) : SimpleEditorCustomization(enabled) {

    override fun customize(editor: EditorEx) {
        val logicalPosition = editor.offsetToLogicalPosition(0)
        editor.scrollingModel.scrollTo(logicalPosition, ScrollType.RELATIVE)

        editor.scrollPane.verticalScrollBarPolicy = ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
        editor.isRendererMode = true
        editor.settings.isLineNumbersShown = true
        editor.setHorizontalScrollbarVisible(false)
        editor.setVerticalScrollbarVisible(true)
        editor.setBorder(null)
    }
}

private val kotlinLanguage = Language.findLanguageByID("kotlin")!!
