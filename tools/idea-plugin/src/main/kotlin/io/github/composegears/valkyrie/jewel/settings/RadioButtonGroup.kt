package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.ui.component.RadioButtonRow
import org.jetbrains.jewel.ui.component.Text

@Composable
fun RadioButtonGroup(
    @Nls text: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(start = 4.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.padding(paddingValues)) {
        Text(text = "$text:")
        Spacer(8.dp)
        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun RadioButtonGroupPreview() = PreviewTheme {
    var selectedOption by rememberMutableState { "Option 1" }

    RadioButtonGroup(
        text = "Label group",
        content = {
            RadioButtonRow(
                text = "Option 1",
                selected = selectedOption == "Option 1",
                onClick = { selectedOption = "Option 1" },
            )
            RadioButtonRow(
                text = "Option 2",
                selected = selectedOption == "Option 2",
                onClick = { selectedOption = "Option 2" },
            )
        },
    )
}
