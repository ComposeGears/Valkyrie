package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.DropdownList
import io.github.composegears.valkyrie.jewel.textfield.ConfirmTextField
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.parser.unified.model.IconType
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconId
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPack
import io.github.composegears.valkyrie.util.IR_STUB
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.PopupMenu
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun ValidIconItem(
    icon: BatchIcon.Valid,
    previewType: PreviewType,
    onUpdatePack: (BatchIcon, String) -> Unit,
    onPreview: (BatchIcon.Valid) -> Unit,
    onDeleteIcon: (IconId) -> Unit,
    onRenameIcon: (BatchIcon, IconName) -> Unit,
    modifier: Modifier = Modifier.Companion,
) {
    ItemCard(modifier = modifier.fillMaxWidth()) {
        CenterVerticalRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconPreviewBox(
                irImageVector = icon.irImageVector,
                previewType = previewType,
                size = 48.dp,
            )
            ConfirmTextField(
                text = icon.iconName.name,
                onValueChange = { onRenameIcon(icon, IconName(it)) },
                errorPlaceholder = "Icon name cannot be empty",
            )
            WeightSpacer()
            if (icon.iconPack is IconPack.Nested) {
                DropdownList(
                    modifier = Modifier.width(100.dp),
                    selected = icon.iconPack.currentNestedPack,
                    items = icon.iconPack.nestedPacks,
                    transform = { it },
                    onSelectItem = { onUpdatePack(icon, it) },
                )
            }
            Box {
                var isMenuVisible by rememberMutableState { false }
                IconButton(
                    onClick = { isMenuVisible = true },
                    focusable = false,
                ) {
                    Icon(
                        key = AllIconsKeys.Actions.More,
                        contentDescription = stringResource("accessibility.more"),
                    )
                }
                if (isMenuVisible) {
                    PopupMenu(
                        onDismissRequest = {
                            isMenuVisible = false
                            true
                        },
                        horizontalAlignment = Alignment.End,
                    ) {
                        selectableItem(
                            selected = false,
                            onClick = { onDeleteIcon(icon.id) },
                            iconKey = AllIconsKeys.General.Delete,
                            content = {
                                Text(text = "Delete")
                            },
                        )
                        selectableItem(
                            selected = false,
                            enabled = icon.iconName.name.isNotEmpty(),
                            onClick = { onPreview(icon) },
                            iconKey = AllIconsKeys.Actions.Preview,
                            content = {
                                Text(text = "Preview")
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .border(
                width = JewelTheme.globalMetrics.outlineWidth,
                color = JewelTheme.globalColors.borders.normal,
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp)),
        content = content,
    )
}

@Preview
@Composable
private fun ValidIconItemPreview() = PreviewTheme {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ValidIconItem(
            icon = BatchIcon.Valid(
                id = IconId("1"),
                iconName = IconName("ic_home"),
                iconType = IconType.SVG,
                irImageVector = IR_STUB,
                iconPack = IconPack.Single(
                    iconPackage = "com.example.icons",
                    iconPackName = "MyIcons",
                ),
            ),
            previewType = PreviewType.White,
            onUpdatePack = { _, _ -> },
            onPreview = {},
            onDeleteIcon = {},
            onRenameIcon = { _, _ -> },
        )

        ValidIconItem(
            icon = BatchIcon.Valid(
                id = IconId("2"),
                iconName = IconName("ic_settings"),
                iconType = IconType.XML,
                irImageVector = IR_STUB,
                iconPack = IconPack.Nested(
                    iconPackage = "com.example.icons",
                    iconPackName = "MyIcons",
                    currentNestedPack = "Outlined",
                    nestedPacks = listOf("Filled", "Outlined", "Rounded"),
                ),
            ),
            previewType = PreviewType.Pixel,
            onUpdatePack = { _, _ -> },
            onPreview = {},
            onDeleteIcon = {},
            onRenameIcon = { _, _ -> },
        )
    }
}
