package io.github.composegears.valkyrie.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.InfoText

@Composable
fun CheckboxSettingsRow(
    @Nls text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    infoText: String? = null,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        CheckboxRow(
            text = text,
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
        infoText?.let {
            InfoText(
                modifier = Modifier.padding(start = 29.dp),
                text = infoText,
            )
        }
    }
}

@Preview
@Composable
private fun CheckboxSettingsRowPreview() = PreviewTheme {
    var checked by rememberMutableState { true }

    CheckboxSettingsRow(
        text = "Test Test Test Test",
        infoText = "Description Description",
        checked = checked,
        onCheckedChange = { checked = it },
    )
}
