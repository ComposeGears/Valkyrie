package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.preview.action.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.util.disabled
import io.github.composegears.valkyrie.ui.foundation.FocusableTextField
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun EditAction(
    iconName: String,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                text = "Name",
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.disabled(),
            )
            FocusableTextField(
                modifier = Modifier.width(200.dp),
                value = iconName,
                onValueChange = onNameChange,
            )
        }
    }
}

@Preview
@Composable
private fun EditActionPreview() = PreviewTheme {
    EditAction(
        iconName = "IconName",
        onNameChange = {},
    )
}
