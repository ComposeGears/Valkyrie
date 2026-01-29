package io.github.composegears.valkyrie.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import dev.snipme.highlights.model.SyntaxLanguage
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.editor.CodeEditor
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val CodePreviewScreen by navDestination<String> {
    val navController = navController()
    val navArgs = navArgs()

    CodePreviewUi(
        code = navArgs,
        onBack = navController::back,
    )
}

@Composable
private fun CodePreviewUi(
    code: String,
    onBack: () -> Unit,
) {
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("code.preview.title"))
        }
        CodeEditor(
            modifier = Modifier.fillMaxSize(),
            syntaxLanguage = SyntaxLanguage.KOTLIN,
            text = code,
            readOnly = true,
            onValueChange = {},
        )
    }
}

@Preview
@Composable
private fun CodePreviewUiPreview() = PreviewTheme {
    CodePreviewUi(
        code = "sample code",
        onBack = {},
    )
}
