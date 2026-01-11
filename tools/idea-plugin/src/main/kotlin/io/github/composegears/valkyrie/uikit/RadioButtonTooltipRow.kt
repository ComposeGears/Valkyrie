package io.github.composegears.valkyrie.uikit

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.RadioButtonRow
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RadioButtonTooltipRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tooltipContent: @Composable () -> Unit,
) {
    CenterVerticalRow(modifier = modifier) {
        RadioButtonRow(
            text = text,
            selected = selected,
            onClick = onClick,
        )
        Spacer(8.dp)
        Tooltip(tooltip = tooltipContent) {
            Icon(
                key = AllIconsKeys.General.ContextHelp,
                contentDescription = stringResource("accessibility.help"),
            )
        }
    }
}

@Preview
@Composable
private fun RadioButtonTooltipRowPreview() = PreviewTheme {
    RadioButtonTooltipRow(
        text = "Radio option",
        selected = false,
        onClick = {},
        tooltipContent = {
            Text(text = "Tooltip content")
        },
    )
}
