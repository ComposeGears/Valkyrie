package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.IconButton
import io.github.composegears.valkyrie.ui.foundation.PreviewWrapper
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.icons.Visibility
import io.github.composegears.valkyrie.ui.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.FileTypeBadge
import java.io.File

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BatchProcessingState(
    icons: List<BatchIcon>,
    modifier: Modifier = Modifier,
    onDeleteIcon: (IconName) -> Unit,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreviewClick: (IconName) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.fillMaxSize(),
        columns = GridCells.Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items = icons, key = { it.iconName }) { batchIcon ->
            when (batchIcon) {
                is BatchIcon.Broken -> BrokenIconItem(
                    modifier = Modifier.animateItemPlacement(),
                    broken = batchIcon,
                    onDelete = onDeleteIcon
                )
                is BatchIcon.Valid -> ValidIconItem(
                    modifier = Modifier.animateItemPlacement(),
                    icon = batchIcon,
                    onUpdatePack = onUpdatePack,
                    onDeleteIcon = onDeleteIcon,
                    onPreview = onPreviewClick
                )
            }
        }
    }
}

@Composable
private fun ValidIconItem(
    modifier: Modifier = Modifier,
    icon: BatchIcon.Valid,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreview: (IconName) -> Unit,
    onDeleteIcon: (IconName) -> Unit
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Box {
            Column {
                FileTypeBadge(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 2.dp, end = 2.dp),
                    extension = icon.extension
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = icon.painter!!,
                        contentDescription = null
                    )
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 32.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        text = icon.iconName.value
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
                            }
                        )
                    }
                    is IconPack.Single -> {
                        Text(
                            modifier = Modifier.padding(16.dp),
                            text = "IconPack: ${icon.iconPack.iconPackName}",
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
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
                    onClick = { isExpanded = true }
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
                    }
                )
            }
        }
    }
}

@Composable
private fun BrokenIconItem(
    modifier: Modifier = Modifier,
    broken: BatchIcon.Broken,
    onDelete: (IconName) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Failed to parse icon: ${broken.iconName.value}.${broken.extension}"
                )
                IconButton(
                    imageVector = Icons.Default.Delete,
                    iconSize = 18.dp,
                    onClick = {
                        onDelete(broken.iconName)
                    }
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
    onPreview: () -> Unit
) {
    DropdownMenu(
        expanded = isExpanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    Text(text = "Delete")
                }
            },
            onClick = onDelete
        )
        DropdownMenuItem(
            text = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = ValkyrieIcons.Visibility, contentDescription = null)
                    Text(text = "Preview")
                }
            },
            onClick = onPreview
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
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val rotation by animateFloatAsState(if (dropdownVisible) -180f else 0f)
            Text(
                text = "${iconPackName}.${currentNestedPack}",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1
            )
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation
                },
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = false }
        ) {
            nestedPacks.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it)
                    },
                    onClick = {
                        dropdownVisible = false
                        onSelectPack(it)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun BatchProcessingStatePreview() = PreviewWrapper {
    BatchProcessingState(
        icons = listOf(
            BatchIcon.Valid(
                iconName = IconName("ic_all_path_params_1"),
                extension = "xml",
                file = File(""),
                iconPack = IconPack.Single(
                    iconPackage = "package",
                    iconPackName = "ValkyrieIcons"
                ),
                painter = painterResource("META-INF/pluginIcon.svg"),
            ),
            BatchIcon.Valid(
                iconName = IconName("ic_all_path_params_2"),
                extension = "svg",
                file = File(""),
                iconPack = IconPack.Nested(
                    iconPackName = "ValkyrieIcons",
                    iconPackage = "package",
                    currentNestedPack = "Kek",
                    nestedPacks = listOf("Lol", "Kek")
                ),
                painter = painterResource("META-INF/pluginIcon.svg"),
            ),
            BatchIcon.Broken(
                iconName = IconName("ic_all_path_params_3"),
                extension = "svg"
            ),
        ),
        onDeleteIcon = {},
        onUpdatePack = { _, _ -> },
        onPreviewClick = {}
    )
}