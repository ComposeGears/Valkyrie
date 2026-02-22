package io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgsOrNull
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.replace
import io.github.composegears.valkyrie.jewel.PreviewCodeAction
import io.github.composegears.valkyrie.jewel.tooling.PreviewNavigationControls
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.ObserveEvent
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackDirectoryPicker
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackScreenScaffold
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.DirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.NestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction.AddNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction.RemoveNestedPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction.SaveDestination
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState.ChooseDestinationDirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.model.NewPackModeState.PickedState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.ui.NewIconPackCreation
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.util.stringResource
import kotlin.random.Random
import org.jetbrains.compose.ui.tooling.preview.Preview

val NewPackScreen by navDestination<PendingPathData> {
    val navController = navController()
    val pendingData = navArgsOrNull()

    val viewModel = viewModel<NewPackViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is NewPackEvent.OnSettingsUpdated -> {
                navController.replace(
                    dest = IconPackConversionScreen,
                    navArgs = pendingData,
                )
            }
            is NewPackEvent.PreviewIconPackObject -> {
                navController.navigate(
                    dest = CodePreviewScreen,
                    navArgs = event.code,
                )
            }
        }
    }

    NewPackContent(
        state = state,
        onBack = navController::back,
        onAction = viewModel::onAction,
        onValueChange = viewModel::onValueChange,
    )
}

@Composable
private fun NewPackContent(
    state: NewPackModeState,
    onBack: () -> Unit,
    onAction: (NewPackAction) -> Unit,
    onValueChange: (InputChange) -> Unit,
) {
    IconPackScreenScaffold(
        title = stringResource("iconpack.newpack.title"),
        onBack = onBack,
        toolbarExtras = {
            WeightSpacer()
            if (state is PickedState && state.inputFieldState.isValid) {
                PreviewCodeAction(onClick = { onAction(PreviewPackObject) })
            }
        },
    ) {
        when (state) {
            is ChooseDestinationDirectoryState -> IconPackDirectoryPicker(
                state = state.directoryState,
                onSelectPath = { onAction(SelectDestinationFolder(it)) },
                onNext = { onAction(SaveDestination) },
            )
            is PickedState -> NewIconPackCreation(
                state = state,
                onAction = onAction,
                onValueChange = onValueChange,
            )
        }
    }
}

@Preview
@Composable
private fun NewPackFlowPreview() = PreviewTheme {
    var state by rememberMutableState<NewPackModeState> {
        ChooseDestinationDirectoryState(
            directoryState = DirectoryState(
                iconPackDestination = "path/to/import",
                predictedPackage = "com.example.iconpack",
                nextAvailable = true,
            ),
        )
    }

    NewPackContent(
        state = state,
        onValueChange = {},
        onBack = {},
        onAction = { action ->
            when (action) {
                is AddNestedPack -> {
                    val picked = state as? PickedState ?: return@NewPackContent

                    val newPack = NestedPack(id = Random.nextLong().toString(), inputFieldState = InputState())

                    state = picked.copy(
                        inputFieldState = picked.inputFieldState.copy(
                            nestedPacks = picked.inputFieldState.nestedPacks + newPack,
                        ),
                    )
                }
                is RemoveNestedPack -> {
                    val picked = state as? PickedState ?: return@NewPackContent

                    state = picked.copy(
                        inputFieldState = picked.inputFieldState.copy(
                            nestedPacks = picked.inputFieldState.nestedPacks.filterNot { it.id == action.nestedPack.id },
                        ),
                    )
                }
                else -> {}
            }
        },
    )
    PreviewNavigationControls(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.BottomCenter),
        onBack = {
            state = ChooseDestinationDirectoryState(
                directoryState = DirectoryState(
                    iconPackDestination = "path/to/import",
                    predictedPackage = "com.example.iconpack",
                    nextAvailable = true,
                ),
            )
        },
        onForward = {
            state = PickedState()
        },
    )
}
