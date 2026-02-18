package io.github.composegears.valkyrie.ui.screen.mode.iconpack.material

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.replace
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.IconPackDirectoryPicker
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.common.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackAction.SelectDestinationFolder
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.model.MaterialPackState.ChooseDestinationDirectoryState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.ui.MaterialPackCreation
import io.github.composegears.valkyrie.util.stringResource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val MaterialPackScreen by navDestination {
    val navController = navController()

    val viewModel = viewModel<MaterialPackViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is MaterialPackEvent.FinishSetup -> {
                        navController.replace(dest = IconPackConversionScreen)
                    }
                }
            }
            .launchIn(this)
    }

    MaterialPackContent(
        state = state,
        onBack = navController::back,
        onAction = viewModel::onAction,
        onValueChange = viewModel::onValueChange,
    )
}

@Composable
private fun MaterialPackContent(
    state: MaterialPackState,
    onBack: () -> Unit,
    onAction: (MaterialPackAction) -> Unit,
    onValueChange: (InputChange) -> Unit,
) {
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(stringResource("iconpack.material.title"))
        }
        VerticallyScrollableContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                when (state) {
                    is ChooseDestinationDirectoryState -> IconPackDirectoryPicker(
                        state = state.directoryState,
                        onSelectPath = { onAction(SelectDestinationFolder(it)) },
                        onNext = { onAction(MaterialPackAction.SaveDestination) },
                    )
                    is MaterialPackState.PickedState -> MaterialPackCreation(
                        state = state,
                        onAction = onAction,
                        onValueChange = onValueChange,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MaterialPackFlowPreview() = ProjectPreviewTheme {
    TiamatPreview(MaterialPackScreen)
}
