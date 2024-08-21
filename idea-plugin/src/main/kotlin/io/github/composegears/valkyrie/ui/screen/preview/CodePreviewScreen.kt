package io.github.composegears.valkyrie.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.navArgs
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.IntellijEditorTextField
import io.github.composegears.valkyrie.ui.foundation.TopAppBar

val CodePreviewScreen by navDestination {
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
    TopAppBar {
      BackAction(onBack = onBack)
    }
    IntellijEditorTextField(
      modifier = Modifier.fillMaxSize(),
      text = code,
    )
  }
}
