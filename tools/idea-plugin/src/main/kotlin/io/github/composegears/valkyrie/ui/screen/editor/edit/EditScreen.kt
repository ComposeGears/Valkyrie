package io.github.composegears.valkyrie.ui.screen.editor.edit

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgs
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.foundation.compositionlocal.LocalProject
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.editor.EditorType
import io.github.composegears.valkyrie.ui.screen.editor.edit.ui.EditorSelectUi
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen

val EditScreen by navDestination<EditorType> {
    val project = LocalProject.current

    val navController = navController()
    val args = navArgs()

    val viewModel = saveableViewModel {
        EditViewModel(
            savedState = it,
            editorType = args,
        )
    }

    val state by viewModel.state.collectAsState()

    EditScreenUi(
        state = state,
        onBack = navController::back,
        openSettings = {
            navController.navigate(dest = SettingsScreen)
        },
        onPickerEvent = {
            viewModel.pickerEvent(project = project.current, events = it)
        },
    )
}

@Composable
private fun EditScreenUi(
    state: EditState,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onPickerEvent: (PickerEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedContent(
            modifier = Modifier.fillMaxSize(),
            targetState = state,
            contentKey = {
                when (it) {
                    is EditState.Select -> 0
                }
            },
            transitionSpec = {
                fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
            },
        ) { current ->
            when (current) {
                is EditState.Select -> {
                    EditorSelectUi(
                        onPickerEvent = onPickerEvent,
                        onBack = onBack,
                        openSettings = openSettings,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun EditScreenPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    EditScreenUi(
        state = EditState.Select,
        onBack = {},
        openSettings = {},
        onPickerEvent = {},
    )
}
