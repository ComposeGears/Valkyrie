package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.foundation.ChoosePackDirectory
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.foundation.NewIconPackCreation
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.ChooseExportDirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.model.NewPackModeState.PickedState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.viewmodel.NewPackViewModel
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val NewPackScreen by navDestination<Unit> {
  val navController = navController()

  val viewModel = koinTiamatViewModel<NewPackViewModel>()
  val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

  LaunchedEffect(Unit) {
    viewModel.events
      .onEach {
        when (it) {
          is NewPackEvent.OnSettingsUpdated -> {
            navController.parent?.navigate(IconPackConversionScreen)
          }
          is NewPackEvent.PreviewIconPackObject -> {
            navController.parent?.navigate(
              dest = CodePreviewScreen,
              navArgs = it.code,
            )
          }
        }
      }
      .launchIn(this)
  }

  NewPackUi(
    state = state,
    onAction = viewModel::onAction,
    onValueChange = viewModel::onValueChange,
  )
}

@Composable
private fun NewPackUi(
  state: NewPackModeState,
  onAction: (NewPackAction) -> Unit,
  modifier: Modifier = Modifier,
  onValueChange: (InputChange) -> Unit,
) {
  Column(modifier = modifier.fillMaxSize()) {
    when (state) {
      is ChooseExportDirectoryState -> {
        ChoosePackDirectory(
          state = state,
          onAction = onAction,
        )
      }
      is PickedState -> {
        NewIconPackCreation(
          state = state,
          onAction = onAction,
          onValueChange = onValueChange,
        )
      }
    }
  }
}

@Preview
@Composable
private fun NewPackFlowPreview() = PreviewTheme {
  NewPackUi(
    modifier = Modifier.fillMaxWidth(0.8f),
    state = ChooseExportDirectoryState(
      iconPackDestination = "path/to/export",
      predictedPackage = "com.example.iconpack",
      nextAvailable = true,
    ),
    onValueChange = {},
    onAction = {},
  )
}
