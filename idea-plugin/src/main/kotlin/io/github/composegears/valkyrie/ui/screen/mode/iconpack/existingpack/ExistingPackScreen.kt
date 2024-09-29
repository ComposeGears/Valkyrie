package io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack

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
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.packedit.model.InputChange
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.foundation.ChooseExistingPackFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.foundation.ExistingPackEditor
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackEvent
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState.ChooserState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.model.ExistingPackModeState.ExistingPackEditState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ui.viewmodel.ExistingPackViewModel
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val ExistingPackScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinTiamatViewModel<ExistingPackViewModel>()
    val state by viewModel.state.collectAsState(Dispatchers.Main.immediate)

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is ExistingPackEvent.OnSettingsUpdated -> {
                        navController.parent?.replace(IconPackConversionScreen)
                    }
                    is ExistingPackEvent.PreviewIconPackObject -> {
                        navController.parent?.navigate(
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
        onAction = viewModel::onAction,
        onValueChange = viewModel::onValueChange,
    )
}

@Composable
private fun ExistingPackUi(
    state: ExistingPackModeState,
    onAction: (ExistingPackAction) -> Unit,
    onValueChange: (InputChange) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        modifier = modifier.fillMaxSize(),
        targetState = state,
        contentKey = {
            when (it) {
                is ChooserState -> 0
                is ExistingPackEditState -> 1
            }
        },
        transitionSpec = {
            fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
        },
    ) { current ->
        when (current) {
            is ChooserState -> {
                ChooseExistingPackFile(onAction = onAction)
            }
            is ExistingPackEditState -> {
                ExistingPackEditor(
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
private fun ExistingPackFlowPreview() = PreviewTheme {
    ExistingPackUi(
        modifier = Modifier.fillMaxWidth(0.8f),
        state = ChooserState,
        onAction = {},
        onValueChange = {},
    )
}
