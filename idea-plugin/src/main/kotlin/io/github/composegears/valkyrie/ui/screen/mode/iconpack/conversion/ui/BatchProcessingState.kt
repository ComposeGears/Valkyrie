package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.parser.IconParser
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Visibility
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.FileTypeBadge
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.IconNameField
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.IconPreviewBox
import kotlin.io.path.Path

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BatchProcessingState(
    icons: List<BatchIcon>,
    onDeleteIcon: (IconName) -> Unit,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreviewClick: (IconName) -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = icons, key = { it.iconName }) { batchIcon ->
            when (batchIcon) {
                is BatchIcon.Broken -> BrokenIconItem(
                    modifier = Modifier.animateItemPlacement(),
                    broken = batchIcon,
                    onDelete = onDeleteIcon,
                )
                is BatchIcon.Valid -> ValidIconItem(
                    modifier = Modifier.animateItemPlacement(),
                    icon = batchIcon,
                    onUpdatePack = onUpdatePack,
                    onDeleteIcon = onDeleteIcon,
                    onPreview = onPreviewClick,
                    onRenameIcon = onRenameIcon,
                )
            }
        }
    }
}

@Composable
private fun ValidIconItem(
    icon: BatchIcon.Valid,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreview: (IconName) -> Unit,
    onDeleteIcon: (IconName) -> Unit,
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
                    extension = icon.extension,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    IconPreviewBox(painter = icon.painter)
                    IconNameField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 32.dp),
                        value = icon.iconName.value,
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
                    imageVector = Icons.Default.MoreVert,
                    onClick = { isExpanded = true },
                )
                IconActionsDropdown(
                    isExpanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    onDelete = {
                        isExpanded = false
                        onDeleteIcon(icon.iconName)
                    },
                    onPreview = {
                        isExpanded = false
                        onPreview(icon.iconName)
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
    onDelete: (IconName) -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
        ),
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 8.dp),
                    text = "Failed to parse icon: ${broken.iconName.value}.${broken.extension}",
                )
                IconButton(
                    imageVector = Icons.Default.Delete,
                    iconSize = 18.dp,
                    onClick = {
                        onDelete(broken.iconName)
                    },
                )
            }
        }
    }
}

@Composable
private fun IconActionsDropdown(
    isExpanded: Boolean,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Text(text = "Delete")
                }
            },
            onClick = onDelete,
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Icon(imageVector = ValkyrieIcons.Visibility, contentDescription = null)
                    Text(text = "Preview")
                }
            },
            onClick = onPreview,
        )
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
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .clickable { dropdownVisible = true }
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
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
                imageVector = Icons.Default.ArrowDropDown,
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
    BatchProcessingState(
        icons = listOf(
            BatchIcon.Valid(
                iconName = IconName(IconParser.getIconName("ic_all_path_params_1")),
                extension = "xml",
                path = Path(""),
                iconPack = IconPack.Single(
                    iconPackage = "package",
                    iconPackName = "ValkyrieIcons",
                ),
                painter = painterResource("META-INF/pluginIcon.svg"),
            ),
            BatchIcon.Broken(
                iconName = IconName("ic_all_path_params_3"),
                extension = "svg",
            ),
            BatchIcon.Valid(
                iconName = IconName(IconParser.getIconName("ic_all_path")),
                extension = "svg",
                path = Path(""),
                iconPack = IconPack.Nested(
                    iconPackName = "ValkyrieIcons",
                    iconPackage = "package",
                    currentNestedPack = "Kek",
                    nestedPacks = listOf("Lol", "Kek"),
                ),
                painter = painterResource("META-INF/pluginIcon.svg"),
            ),
        ),
        onDeleteIcon = {},
        onUpdatePack = { _, _ -> },
        onPreviewClick = {},
        onRenameIcon = { _, _ -> },
    )
}
