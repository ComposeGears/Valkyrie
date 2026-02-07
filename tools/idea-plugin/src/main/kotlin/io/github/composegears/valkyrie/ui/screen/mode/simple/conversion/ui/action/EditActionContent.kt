package io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.ui.action

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.textfield.ConfirmTextField
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun EditActionContent(
    iconName: String,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        HorizontalDivider()
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = stringResource("edit.action.header"),
                style = JewelTheme.typography.small,
            )
            ConfirmTextField(
                modifier = Modifier.width(200.dp),
                text = iconName,
                onValueChange = onNameChange,
                errorPlaceholder = stringResource("edit.action.textfield.error"),
            )
        }
    }
}

@Preview
@Composable
private fun EditActionContentPreview() = PreviewTheme(alignment = Alignment.Center) {
    var name by rememberMutableState { "IconName" }

    EditActionContent(
        iconName = name,
        onNameChange = { name = it },
    )
}
