package io.github.composegears.valkyrie.jewel.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.jewel.tooling.debugBounds
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import org.jetbrains.annotations.Nls
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Text

@Composable
fun InfoSettingsRow(
    @Nls text: String,
    @Nls infoText: String,
    modifier: Modifier = Modifier,
    trailing: (@Composable () -> Unit)? = null,
) {
    CenterVerticalRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(text = text)
            InfoText(text = infoText)
        }
        trailing?.invoke()
    }
}

@Preview
@Composable
private fun InfoSettingsRowPreview() = PreviewTheme(alignment = Alignment.Center) {
    InfoSettingsRow(
        modifier = Modifier
            .padding(horizontal = 32.dp)
            .debugBounds(),
        text = "Title",
        infoText = "Description",
        trailing = {
            Link(text = "Action", onClick = {})
        },
    )
}
