package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import io.github.composegears.valkyrie.jewel.platform.rememberMultiSelectDragAndDropHandler
import io.github.composegears.valkyrie.jewel.tooling.PreviewNavigationControls
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.ui.placeholder.LoadingPlaceholder
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent.PickDirectory
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.IconPackCreationState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.ImportValidationState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.ImportingState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.IconsPickering
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.DragAndDropOverlay
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.BatchProcessingStateUi
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.picker.IconPackPickerStateUi
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import io.github.composegears.valkyrie.util.IR_STUB
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Text

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
                DefaultButton(
                    onClick = onImport,
                    enabled = state.importIssues.isEmpty(),
                ) {
                    Text(text = "Import")
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
        LoadingPlaceholder(text = message)
    }
}

@Preview
@Composable
private fun IconPackConversionUiPickeringPreview() = PreviewTheme {
    var state by rememberMutableState<IconPackConversionState> { IconsPickering }

    IconPackConversionUi(
        state = state,
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

    PreviewNavigationControls(
        modifier = Modifier
            .padding(bottom = 16.dp, end = 16.dp)
            .align(Alignment.BottomEnd),
        onBack = {
            state = IconsPickering
        },
        onForward = {
            state = IconPackCreationState(
                icons = listOf(
                    BatchIcon.Valid(
                        id = IconId("1"),
                        iconName = IconName(IconNameFormatter.format("ic_all_path_params_1")),
                        iconType = IconType.XML,
                        irImageVector = IR_STUB,
                        iconPack = IconPack.Single(
                            iconPackage = "package",
                            iconPackName = "ValkyrieIcons",
                        ),
                    ),
                    BatchIcon.Broken(
                        id = IconId("2"),
                        iconName = IconName(name = "ic_all_path_params_3"),
                        iconSource = IconSource.File,
                    ),
                    BatchIcon.Valid(
                        id = IconId("3"),
                        iconName = IconName(IconNameFormatter.format("ic_all_path")),
                        iconType = IconType.SVG,
                        irImageVector = IR_STUB,
                        iconPack = IconPack.Nested(
                            iconPackName = "ValkyrieIcons",
                            iconPackage = "package",
                            currentNestedPack = "Kek",
                            nestedPacks = listOf("Lol", "Kek"),
                        ),
                    ),
                ),
                importIssues = emptyMap(),
            )
        },
    )
}
