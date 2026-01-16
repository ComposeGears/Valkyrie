package io.github.composegears.valkyrie.jewel.button

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.ToggleableIconActionButton
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipToggleButton(
    key: IconKey,
    contentDescription: String?,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    tooltipText: String,
    modifier: Modifier = Modifier,
) {
    ToggleableIconActionButton(
        modifier = modifier,
        focusable = false,
        key = key,
        contentDescription = contentDescription,
        value = value,
        onValueChange = onValueChange,
        tooltip = {
            Text(text = tooltipText)
        },
    )
}

@Preview
@Composable
private fun TooltipToggleButtonPreview() = PreviewTheme(alignment = Alignment.Center) {
    var checked by rememberMutableState { false }

    TooltipToggleButton(
        key = AllIconsKeys.Actions.Edit,
        contentDescription = null,
        value = checked,
        onValueChange = { checked = it },
        tooltipText = "Tooltip text",
    )
}
