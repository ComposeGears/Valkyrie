package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.lorem
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.annotations.Nls
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LabelTooltipRow(
    text: String,
    @Nls tooltip: String,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(modifier = modifier) {
        Text(text)
        Spacer(8.dp)
        Tooltip(tooltip = { Text(tooltip) }) {
            Icon(
                key = AllIconsKeys.General.ContextHelp,
                contentDescription = stringResource("accessibility.help"),
            )
        }
    }
}

@Preview
@Composable
private fun LabelTooltipRowPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column {
        LabelTooltipRow(
            text = "Package",
            tooltip = lorem(10),
        )
        Spacer(16.dp)
        LabelTooltipRow(
            text = "IconPack name",
            tooltip = lorem(10),
        )
    }
}
