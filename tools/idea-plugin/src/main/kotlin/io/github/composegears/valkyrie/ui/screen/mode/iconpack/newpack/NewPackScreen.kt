package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navArgsOrNull
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.replace
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
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

val NewPackScreen by navDestination<PendingPathData> {
    val navController = navController()
    val pendingData = navArgsOrNull()

    val viewModel = viewModel<NewPackViewModel>()
    val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is NewPackEvent.OnSettingsUpdated -> {
                        navController.parent?.replace(
                            dest = IconPackConversionScreen,
                            navArgs = pendingData,
                        )
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
    AnimatedContent(
        modifier = modifier.fillMaxSize(),
        targetState = state,
        contentKey = {
            when (it) {
                is ChooseExportDirectoryState -> 0
                is PickedState -> 1
            }
        },
        transitionSpec = {
            fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
        },
    ) { current ->
        when (current) {
            is ChooseExportDirectoryState -> {
                ChoosePackDirectory(
                    state = current,
                    onAction = onAction,
                )
            }
            is PickedState -> {
                NewIconPackCreation(
                    state = current,
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
