package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun SwitchOption(
    title: String,
    description: String,
    checked: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit,
) {
    ListItem(
        modifier = modifier
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange,
            )
            .padding(horizontal = 8.dp)
            .heightIn(max = 100.dp),
        headlineContent = {
            Text(text = title)
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = LocalContentColor.current.dim(),
            )
        },
        trailingContent = {
            Switch(
                modifier = Modifier.scale(0.9f),
                checked = checked,
                onCheckedChange = onCheckedChange,
            )
        },
    )
}

@Preview
@Composable
private fun SwitchOptionPreview() = PreviewTheme {
    Column {
        SwitchOption(
            title = "Example Option",
            description = "This is a description of what this option does",
            checked = false,
            onCheckedChange = {},
        )

        SwitchOption(
            title = "Enabled Option",
            description = "This option is currently enabled",
            checked = true,
            onCheckedChange = {},
        )
    }
}
