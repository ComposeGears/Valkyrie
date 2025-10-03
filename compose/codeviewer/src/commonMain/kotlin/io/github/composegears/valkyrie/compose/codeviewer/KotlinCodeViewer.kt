package io.github.composegears.valkyrie.compose.codeviewer

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import io.github.composegears.valkyrie.compose.codeviewer.core.CodeEditor
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.util.isLight

@Composable
fun KotlinCodeViewer(
    text: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit = {},
) {
    val isLight = MaterialTheme.colorScheme.isLight

    var highlights by rememberMutableState(isLight, text) {
        Highlights.Builder()
            .code(text)
            .language(SyntaxLanguage.KOTLIN)
            .theme(SyntaxThemes.darcula(darkMode = !isLight))
            .build()
    }
    CodeEditor(
        modifier = modifier,
        highlights = highlights,
        onValueChange = onChange,
        textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp, fontSize = 13.sp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
    )
}

// @Preview - not working without Android target
@Composable
private fun CodePreview() {
    KotlinCodeViewer(
        text = """
        val ValkyrieIcons.EmptyImageVector: ImageVector
            get() {
                if (_EmptyImageVector != null) {
                    return _EmptyImageVector!!
                }
                _EmptyImageVector = ImageVector.Builder(
                    name = "EmptyImageVector",
                    defaultWidth = 24.dp,
                    defaultHeight = 24.dp,
                    viewportWidth = 18f,
                    viewportHeight = 18f,
                ).build()

                return _EmptyImageVector!!
            }
        """.trimIndent(),
    )
}
