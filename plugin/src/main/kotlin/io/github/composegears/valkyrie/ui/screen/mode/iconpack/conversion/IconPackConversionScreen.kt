package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.koin.koinTiamatViewModel
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.ClearAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.WeightSpacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchFilesProcessing
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.BatchProcessingState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.IconPackPickerState
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val IconPackConversionScreen by navDestination<Unit> {
    val navController = navController()

    val viewModel = koinTiamatViewModel<IconPackConversionViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is ConversionEvent.OpenPreview -> {
                        navController.navigate(
                            dest = CodePreviewScreen,
                            navArgs = it.iconContent
                        )
                    }
                    is ConversionEvent.ExportCompleted -> {
                        writeAction {
                            VirtualFileManager.getInstance().syncRefresh()
                        }
                    }
                }
            }.launchIn(this)
    }

    IconPackConversionUi(
        state = state,
        openSettings = {
            navController.navigate(
                dest = SettingsScreen,
                transition = navigationSlideInOut(true)
            )
        },
        onPickEvent = viewModel::pickerEvent,
        updatePack = viewModel::updateIconPack,
        onDeleteIcon = viewModel::deleteIcon,
        onReset = viewModel::reset,
        onPreviewClick = viewModel::showPreview,
        onExport = viewModel::export,
        onRenameIcon = viewModel::renameIcon
    )
}

@Composable
private fun IconPackConversionUi(
    state: IconPackConversionState,
    openSettings: () -> Unit,
    onPickEvent: (PickerEvent) -> Unit,
    updatePack: (BatchIcon, String) -> Unit,
    onDeleteIcon: (IconName) -> Unit,
    onReset: () -> Unit,
    onPreviewClick: (IconName) -> Unit,
    onExport: () -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit
) {
    var isVisible by rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) {
                    isVisible = false
                }
                if (available.y > 1) {
                    isVisible = true
                }

                return Offset.Zero
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier
        .pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar {
                if (state is BatchFilesProcessing) {
                    ClearAction(onReset)
                }
                AppBarTitle(title = "IconPack generation")
                WeightSpacer()
                SettingsAction(openSettings = openSettings)
            }
            when (state) {
                is IconsPickering -> {
                    IconPackPickerState(onPickerEvent = onPickEvent)
                }
                is BatchFilesProcessing -> {
                    BatchProcessingState(
                        modifier = Modifier.nestedScroll(nestedScrollConnection),
                        icons = state.iconsToProcess,
                        onDeleteIcon = onDeleteIcon,
                        onUpdatePack = updatePack,
                        onPreviewClick = onPreviewClick,
                        onRenameIcon = onRenameIcon
                    )
                }
            }
        }

        if (state is BatchFilesProcessing) {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                visible = isVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                Button(
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp),
                    enabled = state.exportEnabled,
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors().copy(
                        disabledContainerColor = Color.Gray,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 6.dp,
                        focusedElevation = 6.dp,
                        disabledElevation = 0.dp
                    ),
                    onClick = onExport
                ) {
                    Text(
                        text = "Export",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}