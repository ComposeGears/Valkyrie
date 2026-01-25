package io.github.composegears.valkyrie.jewel.button

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.ToggleableIconButton
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.painter.badge.DotBadgeShape
import org.jetbrains.jewel.ui.painter.hints.Badge

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipHintToggleButton(
    key: IconKey,
    contentDescription: String?,
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    tooltipText: String,
    modifier: Modifier = Modifier,
) {
    Tooltip(
        tooltip = {
            Text(text = tooltipText)
        },
    ) {
        ToggleableIconButton(
            modifier = modifier,
            focusable = false,
            value = value,
            onValueChange = onValueChange,
        ) { _ ->
            Icon(
                key = key,
                contentDescription = contentDescription,
                hints = arrayOf(Badge(Color.Red, DotBadgeShape.Default)),
            )
        }
    }
}

@Preview
@Composable
private fun TooltipHintToggleButtonPreview() = PreviewTheme(alignment = Alignment.Center) {
    var value by rememberMutableState { false }

    TooltipHintToggleButton(
        key = AllIconsKeys.Toolwindows.Notifications,
        contentDescription = null,
        value = value,
        onValueChange = { value = it },
        tooltipText = "Tooltip text",
    )
}
