package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.OutlineIconButton
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.BatchIcon
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconId
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconSource
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.Outline
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.outline

@Composable
fun BrokenIconItem(
    broken: BatchIcon.Broken,
    modifier: Modifier = Modifier.Companion,
    onDelete: (IconId) -> Unit,
) {
    CenterVerticalRow(
        modifier = modifier
            .fillMaxWidth()
            .outline(
                outline = Outline.Error,
                focused = true,
                outlineShape = RoundedCornerShape(8.dp),
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Text(
            modifier = Modifier.Companion.weight(1f),
            text = when (broken.iconSource) {
                IconSource.File -> "Failed to parse icon: ${broken.iconName.name}"
                IconSource.Clipboard -> "Failed to parse icon"
            },
        )
        OutlineIconButton(
            key = AllIconsKeys.General.Delete,
            contentDescription = stringResource("accessibility.remove"),
            onClick = { onDelete(broken.id) },
        )
    }
}

@Preview
@Composable
private fun BrokenIconItemPreview() = PreviewTheme {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        BrokenIconItem(
            broken = BatchIcon.Broken(
                id = IconId("1"),
                iconName = IconName("ic_broken_icon"),
                iconSource = IconSource.File,
            ),
            onDelete = {},
        )
        BrokenIconItem(
            broken = BatchIcon.Broken(
                id = IconId("2"),
                iconName = IconName(""),
                iconSource = IconSource.Clipboard,
            ),
            onDelete = {},
        )
    }
}
