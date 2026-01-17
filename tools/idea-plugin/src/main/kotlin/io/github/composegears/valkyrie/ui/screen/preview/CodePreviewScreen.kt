package io.github.composegears.valkyrie.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.sdk.compose.codeviewer.KotlinCodeViewer

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
        }
        KotlinCodeViewer(
            modifier = Modifier.fillMaxSize(),
            text = code,
        )
    }
}
