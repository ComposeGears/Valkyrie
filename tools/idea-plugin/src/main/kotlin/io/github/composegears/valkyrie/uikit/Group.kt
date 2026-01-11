package io.github.composegears.valkyrie.uikit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import org.jetbrains.annotations.Nls
import org.jetbrains.jewel.ui.component.GroupHeader

@Composable
fun Group(
    @Nls text: String,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 16.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier.padding(paddingValues)) {
        GroupHeader(text = text)

        Column(
            modifier = Modifier.padding(start = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Spacer(8.dp)
            content()
        }
    }
}

@Suppress("ktlint:compose:modifier-missing-check")
@Composable
fun ColumnScope.GroupSpacing() = Spacer(32.dp)

@Preview
@Composable
internal fun GroupPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    var checked by rememberMutableState { true }

    Group(
        text = "Group header",
        content = {
            CheckboxRow(
                text = "Option",
                infoText = "Option description",
                checked = checked,
                onCheckedChange = { checked = it },
            )
        },
    )
}
