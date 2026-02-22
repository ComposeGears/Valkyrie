package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

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
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackScreenScaffold
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackAction.PreviewPackObject
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState.ChooserState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.model.ExistingPackModeState.ExistingPackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.ChooseExistingPackFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.ExistingPackEditor
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val ExistingPackScreen by navDestination<PendingPathData> {
    val navController = navController()
    val pendingData = navArgsOrNull()

    val viewModel = viewModel<ExistingPackViewModel>()
    val state by viewModel.state.collectAsState()

    ObserveEvent(viewModel.events) { event ->
        when (event) {
            is ExistingPackEvent.OnSettingsUpdated -> {
                navController.replace(
                    dest = IconPackConversionScreen,
                    navArgs = pendingData,
                )
            }
            is ExistingPackEvent.PreviewIconPackObject -> {
                navController.navigate(
                    dest = CodePreviewScreen,
                    navArgs = event.code,
                )
            }
        }
    }

    ExistingPackUi(
        state = state,
        onBack = navController::back,
        onAction = viewModel::onAction,
        onValueChange = viewModel::onValueChange,
    )
}

@Composable
private fun ExistingPackUi(
    state: ExistingPackModeState,
    onBack: () -> Unit,
    onAction: (ExistingPackAction) -> Unit,
    onValueChange: (InputChange) -> Unit,
) {
    IconPackScreenScaffold(
        title = stringResource("iconpack.existing.pack.title"),
        onBack = onBack,
        toolbarExtras = {
            WeightSpacer()
            if (state is ExistingPackEditState && state.inputFieldState.isValid) {
                PreviewCodeAction(onClick = { onAction(PreviewPackObject) })
            }
        },
    ) {
        when (state) {
            is ChooserState -> {
                ChooseExistingPackFile(onAction = onAction)
            }
            is ExistingPackEditState -> {
                ExistingPackEditor(
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
private fun ExistingPackFlowPreview() = PreviewTheme {
    var state by rememberMutableState<ExistingPackModeState> { ChooserState }

    ExistingPackUi(
        state = state,
        onBack = {},
        onAction = {},
        onValueChange = {},
    )

    PreviewNavigationControls(
        modifier = Modifier
            .padding(bottom = 16.dp)
            .align(Alignment.BottomCenter),
        onBack = { state = ChooserState },
        onForward = { state = ExistingPackEditState() },
    )
}
