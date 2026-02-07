package io.github.composegears.valkyrie.jewel.editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.edtWriteAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.event.DocumentEvent
import com.intellij.openapi.editor.event.DocumentListener
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.util.Disposer
import io.github.composegears.valkyrie.jewel.platform.LocalProject
import io.github.composegears.valkyrie.jewel.tooling.PreviewNavigationControls
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.InfoText

enum class SyntaxLanguage {
    KOTLIN, XML
}

@Composable
fun IntellijEditorTextField(
    text: String,
    language: SyntaxLanguage,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
) {
    val editor = rememberIntelliJEditor(
        text = text,
        language = language,
        readOnly = readOnly,
        onValueChange = onValueChange
    )

    editor?.let { editor ->
        SwingPanel(
            modifier = modifier,
            factory = { editor.component },
        )
    }
}

@Composable
private fun rememberIntelliJEditor(
    text: String,
    language: SyntaxLanguage,
    readOnly: Boolean,
    onValueChange: (String) -> Unit,
): Editor? {
    val project = LocalProject.current

    var editor by rememberMutableState<Editor?> { null }
    var document by rememberMutableState<Document?> { null }

    DisposableEffect(project, language) {
        val scope = CoroutineScope(Dispatchers.EDT)
        val disposable = Disposer.newDisposable("intellij-editor")

        scope.launch {
            val fileName = when (language) {
                SyntaxLanguage.KOTLIN -> "Temp.kt"
                SyntaxLanguage.XML -> "Temp.xml"
            }

            val fileType = FileTypeManager.getInstance().getFileTypeByFileName(fileName)

            val doc = EditorFactory.getInstance().createDocument(text).also {
                document = it
            }

            val createdEditor = EditorFactory.getInstance()
                .createEditor(doc, project, fileType, readOnly) as EditorEx

            createdEditor.apply {
                settings.isLineNumbersShown = true
                settings.isAutoCodeFoldingEnabled = false
                settings.setGutterIconsShown(false)
                setBorder(null)
            }

            val listener = object : DocumentListener {
                override fun documentChanged(event: DocumentEvent) {
                    onValueChange(event.document.text)
                }
            }
            doc.addDocumentListener(listener, disposable)
            editor = createdEditor
        }

        onDispose {
            scope.launch {
                Disposer.dispose(disposable)
                editor?.let(EditorFactory.getInstance()::releaseEditor)
                editor = null
                document = null
            }
        }
    }

    LaunchedEffect(text) {
        val doc = document ?: return@LaunchedEffect

        if (doc.text != text) {
            edtWriteAction {
                doc.setText(text)
            }
        }
    }

    return editor
}

@Preview
@Composable
private fun IntellijEditorTextFieldPreview() = ProjectPreviewTheme {
    val kotlinCode = """        
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.graphics.SolidColor
        import androidx.compose.ui.graphics.vector.ImageVector
        import androidx.compose.ui.graphics.vector.path
        import androidx.compose.ui.unit.dp
        import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
        
        val ValkyrieIcons.Outlined.Tune: ImageVector
            get() {
                if (_Tune != null) {
                    return _Tune!!
                }
                _Tune = ImageVector.Builder(
                    name = "Outlined.Tune",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 960f,
                    viewportHeight = 960f,
                ).apply {
                    path(fill = SolidColor(Color.Black)) {
                        close()
                    }
                }.build()
        
                return _Tune!!
            }
        
        @Suppress("ObjectPropertyName")
        private var _Tune: ImageVector? = null
        
        """.trimIndent()

    val xmlCode = """        
        <vector xmlns:android="http://schemas.android.com/apk/res/android"
            android:width="24dp"
            android:height="24dp"
            android:viewportWidth="18"
            android:viewportHeight="18"
            android:autoMirrored="true">
            <path
                android:name="path_name"
                android:pathData="M6.75,12.127L3.623,9L2.558,10.057L6.75,14.25L15.75,5.25L14.693,4.192L6.75,12.127Z"
                android:strokeWidth="1"
                android:fillAlpha="0.5"
                android:strokeColor="#232F34"
                android:strokeAlpha="0.5"
                android:strokeLineCap="round"
                android:strokeLineJoin="round"
                android:strokeMiterLimit="3"
                android:fillType="evenOdd"
                android:fillColor="#232F34" />
        </vector>

        """.trimIndent()

    var changedText by rememberMutableState { "" }
    var currentLanguage by rememberMutableState { SyntaxLanguage.KOTLIN }

    Column {
        when (currentLanguage) {
            SyntaxLanguage.KOTLIN -> {
                IntellijEditorTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = kotlinCode,
                    language = currentLanguage,
                    onValueChange = { changedText = it }
                )

            }
            SyntaxLanguage.XML -> {
                IntellijEditorTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = xmlCode,
                    language = currentLanguage,
                    onValueChange = { changedText = it }
                )
            }
        }

        PreviewNavigationControls(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onBack = { currentLanguage = SyntaxLanguage.KOTLIN },
            onForward = { currentLanguage = SyntaxLanguage.XML },
        )

        InfoText(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .height(88.dp)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            text = changedText,
        )
    }
}