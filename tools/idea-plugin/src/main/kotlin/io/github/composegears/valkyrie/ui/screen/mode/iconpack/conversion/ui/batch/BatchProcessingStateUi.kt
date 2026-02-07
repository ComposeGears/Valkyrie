package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.CloseAction
import io.github.composegears.valkyrie.jewel.NotificationToggleAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.platform.ClipboardDataType
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.ExpandedAnimatedContent
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.ClipboardEventColumn
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent.ClipboardText
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconId
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionState.BatchProcessing.IconPackCreationState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.FailedToParseFile
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.HasDuplicates
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameContainsSpace
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError.IconNameEmpty
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.model.BatchAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.BrokenIconItem
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.ImportIssuesUI
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.ValidIconItem
import io.github.composegears.valkyrie.util.IR_STUB
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.VerticalScrollbar

@Composable
fun BatchProcessingStateUi(
    state: IconPackCreationState,
    previewType: PreviewType,
    onScrollUnavailable: () -> Unit,
    onPasteEvent: (PickerEvent) -> Unit,
    onClose: () -> Unit,
    openSettings: () -> Unit,
    onDeleteIcon: (IconId) -> Unit,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreviewClick: (BatchIcon.Valid) -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit,
    onResolveIssues: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val latestOnScrollUnavailable by rememberUpdatedState(onScrollUnavailable)
    var currentAction by rememberMutableState<BatchAction>(state) { BatchAction.None }

    ClipboardEventColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        onPaste = { dataType ->
            when (dataType) {
                is ClipboardDataType.Files -> onPasteEvent(PickFiles(paths = dataType.paths))
                is ClipboardDataType.Text -> onPasteEvent(ClipboardText(dataType.text))
            }
        },
    ) {
        Toolbar {
            CloseAction(onClose = onClose)
            Title(text = "IconPack generation")
            WeightSpacer()
            if (state.importIssues.isNotEmpty()) {
                NotificationToggleAction(
                    selected = currentAction is BatchAction.ImportIssues,
                    onNotification = { selected ->
                        currentAction = if (selected) BatchAction.ImportIssues(state.importIssues) else BatchAction.None
                    },
                )
            }
            SettingsAction(openSettings = openSettings)
        }
        ProcessingActions(
            action = currentAction,
            onResolveIssues = onResolveIssues,
        )
        Box {
            val lazyGridState = rememberLazyGridState()

            LaunchedEffect(lazyGridState.canScrollBackward, lazyGridState.canScrollForward) {
                val canScroll = lazyGridState.canScrollBackward || lazyGridState.canScrollForward
                if (!canScroll) latestOnScrollUnavailable()
            }

            LazyVerticalGrid(
                state = lazyGridState,
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(300.dp),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items = state.icons, key = { it.id }) { batchIcon ->
                    when (batchIcon) {
                        is BatchIcon.Broken -> BrokenIconItem(
                            broken = batchIcon,
                            onDelete = onDeleteIcon,
                        )
                        is BatchIcon.Valid -> ValidIconItem(
                            icon = batchIcon,
                            previewType = previewType,
                            onUpdatePack = onUpdatePack,
                            onDeleteIcon = onDeleteIcon,
                            onPreview = onPreviewClick,
                            onRenameIcon = onRenameIcon,
                        )
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp, top = 8.dp, bottom = 4.dp),
                scrollState = lazyGridState,
            )
        }
    }
}

@Composable
private fun ProcessingActions(
    action: BatchAction,
    onResolveIssues: () -> Unit,
) {
    ExpandedAnimatedContent(
        modifier = Modifier.fillMaxWidth(),
        targetState = action,
    ) { action ->
        when (action) {
            is BatchAction.ImportIssues -> ImportIssuesUI(
                importIssues = action.issues,
                onResolveIssues = onResolveIssues,
            )
            BatchAction.None -> Spacer(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun BatchProcessingStatePreview() = PreviewTheme {
    BatchProcessingStateUi(
        state = IconPackCreationState(
            importIssues = mapOf(
                IconNameEmpty to listOf(IconName("")),
                IconNameContainsSpace to listOf(IconName("Ic Duplicate")),
                FailedToParseFile to listOf(IconName("test.svg")),
                HasDuplicates to listOf(IconName("IcDuplicate")),
            ),
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
        ),
        previewType = PreviewType.Auto,
        onScrollUnavailable = {},
        onPasteEvent = {},
        onClose = {},
        openSettings = {},
        onDeleteIcon = {},
        onUpdatePack = { _, _ -> },
        onPreviewClick = {},
        onRenameIcon = { _, _ -> },
        onResolveIssues = { },
    )
}
