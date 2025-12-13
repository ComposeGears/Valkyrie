package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.animation.ExpandedAnimatedContent
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.ui.foundation.VerticalScrollbar
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.parser.unified.util.IconNameFormatter
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.ClipboardText
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent.PickFiles
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.CloseAction
import io.github.composegears.valkyrie.ui.foundation.FocusableTextField
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.NotificationAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.icons.ArrowDropDown
import io.github.composegears.valkyrie.ui.foundation.icons.Delete
import io.github.composegears.valkyrie.ui.foundation.icons.MoreVert
import io.github.composegears.valkyrie.ui.foundation.icons.Visibility
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.platform.ClipboardDataType
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
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.ClipboardEventColumn
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.model.BatchAction
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.FileTypeBadge
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.IconPreviewBox
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui.ImportIssuesUI
import io.github.composegears.valkyrie.util.IR_STUB

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
        TopAppBar {
            CloseAction(onClose = onClose)
            AppBarTitle(title = "IconPack generation")
            WeightSpacer()
            if (state.importIssues.isNotEmpty()) {
                NotificationAction(
                    selected = currentAction is BatchAction.ImportIssues,
                    onNotification = {
                        currentAction = when (currentAction) {
                            is BatchAction.ImportIssues -> BatchAction.None
                            else -> BatchAction.ImportIssues(state.importIssues)
                        }
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
            VerticalScrollbar(adapter = rememberScrollbarAdapter(lazyGridState))
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

@Composable
private fun ValidIconItem(
    icon: BatchIcon.Valid,
    previewType: PreviewType,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreview: (BatchIcon.Valid) -> Unit,
    onDeleteIcon: (IconId) -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Box {
            Column {
                FileTypeBadge(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp, end = 2.dp),
                    text = icon.iconType.extension,
                )
                CenterVerticalRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    IconPreviewBox(
                        irImageVector = icon.irImageVector,
                        previewType = previewType,
                    )

                    val name = icon.iconName.name
                    FocusableTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 32.dp),
                        value = name,
                        onValueChange = {
                            onRenameIcon(icon, IconName(it))
                        },
                    )
                }
                when (icon.iconPack) {
                    is IconPack.Nested -> {
                        PacksDropdown(
                            iconPackName = icon.iconPack.iconPackName,
                            currentNestedPack = icon.iconPack.currentNestedPack,
                            nestedPacks = icon.iconPack.nestedPacks,
                            onSelectPack = {
                                onUpdatePack(icon, it)
                            },
                        )
                    }
                    is IconPack.Single -> {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "IconPack: ${icon.iconPack.iconPackName}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 2.dp),
            ) {
                var isExpanded by rememberMutableState { false }

                IconButton(
                    imageVector = ValkyrieIcons.MoreVert,
                    onClick = { isExpanded = true },
                )
                IconActionsDropdown(
                    isExpanded = isExpanded,
                    previewEnabled = { icon.iconName.name.isNotEmpty() },
                    onDismissRequest = { isExpanded = false },
                    onDelete = {
                        isExpanded = false
                        onDeleteIcon(icon.id)
                    },
                    onPreview = {
                        isExpanded = false
                        onPreview(icon)
                    },
                )
            }
        }
    }
}

@Composable
private fun BrokenIconItem(
    broken: BatchIcon.Broken,
    modifier: Modifier = Modifier,
    onDelete: (IconId) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
    ) {
        Column {
            CenterVerticalRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    text = when (broken.iconSource) {
                        IconSource.File -> "Failed to parse icon: ${broken.iconName.name}"
                        IconSource.Clipboard -> "Failed to parse icon"
                    },
                )
                IconButton(
                    imageVector = ValkyrieIcons.Delete,
                    iconSize = 18.dp,
                    onClick = {
                        onDelete(broken.id)
                    },
                )
            }
        }
    }
}

@Composable
private fun IconActionsDropdown(
    isExpanded: Boolean,
    previewEnabled: () -> Boolean,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit,
    onPreview: () -> Unit,
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(
            text = {
                CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(imageVector = ValkyrieIcons.Delete, contentDescription = null)
                    Text(text = "Delete")
                }
            },
            onClick = onDelete,
        )
        if (previewEnabled()) {
            DropdownMenuItem(
                text = {
                    CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = ValkyrieIcons.Visibility, contentDescription = null)
                        Text(text = "Preview")
                    }
                },
                onClick = onPreview,
            )
        }
    }
}

@Composable
private fun PacksDropdown(
    iconPackName: String,
    currentNestedPack: String,
    nestedPacks: List<String>,
    onSelectPack: (String) -> Unit,
) {
    var dropdownVisible by rememberMutableState { false }

    Box(modifier = Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp)) {
        CenterVerticalRow(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { dropdownVisible = true }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val rotation by animateFloatAsState(if (dropdownVisible) -180f else 0f)
            Text(
                text = "$iconPackName.$currentNestedPack",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
            )
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                },
                imageVector = ValkyrieIcons.ArrowDropDown,
                contentDescription = null,
            )
        }

        DropdownMenu(
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = false },
        ) {
            nestedPacks.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        dropdownVisible = false
                        onSelectPack(it)
                    },
                )
            }
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
