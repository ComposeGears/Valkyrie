package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.PreviewCodeAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewNavigationControls
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val ExistingPackScreen by navDestination<PendingPathData> {
    val navController = navController()
    val pendingData = navArgsOrNull()

    val viewModel = viewModel<ExistingPackViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is ExistingPackEvent.OnSettingsUpdated -> {
                        navController.replace(
                            dest = IconPackConversionScreen,
                            navArgs = pendingData,
                        )
                    }
                    is ExistingPackEvent.PreviewIconPackObject -> {
                        navController.navigate(
                            dest = CodePreviewScreen,
                            navArgs = it.code,
                        )
                    }
                }
            }
            .launchIn(this)
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
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(stringResource("iconpack.existing.pack.title"))
            WeightSpacer()
            WeightSpacer()
            if (state is ExistingPackEditState && state.inputFieldState.isValid) {
                PreviewCodeAction(onClick = { onAction(PreviewPackObject) })
            }
        }
        VerticallyScrollableContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
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
