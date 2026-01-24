package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navArgsOrNull
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import com.composegears.tiamat.compose.saveableViewModel
import com.intellij.openapi.application.writeAction
import com.intellij.openapi.vfs.VirtualFileManager
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.WarningBanner
import io.github.composegears.valkyrie.jewel.banner.rememberBannerManager
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.IconPackCreationState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.ImportValidationState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.ImportingState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.DragAndDropOverlay
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.BatchProcessingStateUi
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.picker.IconPackPickerStateUi
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

val IconPackConversionScreen by navDestination<PendingPathData> {
    val navController = navController()
    val pendingData = navArgsOrNull()

    val viewModel = saveableViewModel {
        IconPackConversionViewModel(
            savedState = it,
            paths = pendingData?.paths.orEmpty(),
        )
    }
    val bannerManager = rememberBannerManager()
    val state by viewModel.state.collectAsState()
    val settings by viewModel.inMemorySettings.settings.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events
            .onEach {
                when (it) {
                    is ConversionEvent.OpenPreview -> {
                        navController.navigate(
                            dest = CodePreviewScreen,
                            navArgs = it.iconContent,
                        )
                    }
                    is ConversionEvent.ImportCompleted -> {
                        @Suppress("UnstableApiUsage")
                        writeAction {
                            VirtualFileManager.getInstance().syncRefresh()
                        }
                    }
                    is ConversionEvent.NothingToImport -> {
                        bannerManager.show(message = WarningBanner(text = message("iconpack.conversion.nothing.import")))
                    }
                }
            }.launchIn(this)
    }

    IconPackConversionUi(
        state = state,
        previewType = settings.previewType,
        onBack = navController::back,
        openSettings = {
            navController.navigate(dest = SettingsScreen)
        },
        onPickEvent = viewModel::pickerEvent,
        updatePack = viewModel::updateIconPack,
        onDeleteIcon = viewModel::deleteIcon,
        onReset = viewModel::reset,
        onPreviewClick = viewModel::showPreview,
        onImport = viewModel::import,
        onRenameIcon = viewModel::renameIcon,
        onResolveIssues = viewModel::resolveImportIssues,
    )
}

@Composable
private fun IconPackConversionUi(
    state: IconPackConversionState,
    previewType: PreviewType,
    onBack: () -> Unit,
    openSettings: () -> Unit,
    onPickEvent: (PickerEvent) -> Unit,
    updatePack: (BatchIcon, String) -> Unit,
    onDeleteIcon: (IconId) -> Unit,
    onReset: () -> Unit,
    onPreviewClick: (BatchIcon.Valid) -> Unit,
    onImport: () -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit,
    onResolveIssues: () -> Unit,
) {
    var isImportButtonVisible by rememberSaveable { mutableStateOf(true) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < -1) {
                    isImportButtonVisible = false
                }
                if (available.y > 1) {
                    isImportButtonVisible = true
                }

                return Offset.Zero
            }
        }
    }

    Box {
        Column(modifier = Modifier.fillMaxSize()) {
            AnimatedContent(
                modifier = Modifier.fillMaxSize(),
                targetState = state,
                contentKey = {
                    when (it) {
                        is IconsPickering -> 0
                        is ImportingState -> 1
                        is IconPackCreationState -> 2
                        is ImportValidationState -> 3
                    }
                },
                transitionSpec = {
                    fadeIn(tween(220, delayMillis = 90)) togetherWith fadeOut(tween(90))
                },
            ) { current ->
                when (current) {
                    is IconsPickering -> {
                        IconPackPickerStateUi(
                            onPickerEvent = onPickEvent,
                            onBack = onBack,
                            openSettings = openSettings,
                        )
                    }
                    ImportingState -> {
                        LoadingStateUi(message = "Importing icons...")
                    }
                    ImportValidationState -> {
                        LoadingStateUi(message = "Processing icons...")
                    }
                    is IconPackCreationState -> {
                        BatchProcessingStateUi(
                            modifier = Modifier.nestedScroll(nestedScrollConnection),
                            state = current,
                            previewType = previewType,
                            onScrollUnavailable = { isImportButtonVisible = true },
                            onPasteEvent = onPickEvent,
                            onDeleteIcon = onDeleteIcon,
                            onUpdatePack = updatePack,
                            onPreviewClick = onPreviewClick,
                            onRenameIcon = onRenameIcon,
                            onClose = onReset,
                            openSettings = openSettings,
                            onResolveIssues = onResolveIssues,
                        )
                    }
                }
            }
        }

        if (state is IconPackCreationState) {
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                visible = isImportButtonVisible,
                enter = slideInVertically(initialOffsetY = { it * 2 }),
                exit = slideOutVertically(targetOffsetY = { it * 2 }),
            ) {
                Button(
                    modifier = Modifier.defaultMinSize(minHeight = 36.dp),
                    enabled = state.importIssues.isEmpty(),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors().copy(
                        disabledContainerColor = Color.Gray,
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 6.dp,
                        pressedElevation = 6.dp,
                        focusedElevation = 6.dp,
                        disabledElevation = 0.dp,
                    ),
                    onClick = onImport,
                ) {
                    Text(
                        text = "Import",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            val dragAndDropHandler = rememberMultiSelectDragAndDropHandler(
                onDrop = { paths ->
                    when {
                        paths.size == 1 -> {
                            val path = paths.first()

                            when {
                                path.isDirectory() -> onPickEvent(PickDirectory(path = path))
                                path.isRegularFile() -> onPickEvent(PickFiles(paths = paths))
                            }
                        }
                        else -> onPickEvent(PickFiles(paths = paths))
                    }
                },
            )
            DragAndDropOverlay(isDragging = dragAndDropHandler.isDragging)
        }
    }
}

@Composable
private fun LoadingStateUi(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator()
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun IconPackConversionUiPickeringPreview() = PreviewTheme {
    IconPackConversionUi(
        state = IconsPickering,
        previewType = PreviewType.Auto,
        onBack = {},
        openSettings = {},
        onPickEvent = {},
        updatePack = { _, _ -> },
        onDeleteIcon = {},
        onReset = {},
        onPreviewClick = {},
        onImport = {},
        onRenameIcon = { _, _ -> },
        onResolveIssues = {},
    )
}

@Preview
@Composable
private fun LoadingStateUiPreview() = PreviewTheme {
    LoadingStateUi(message = "Importing icons...")
}
