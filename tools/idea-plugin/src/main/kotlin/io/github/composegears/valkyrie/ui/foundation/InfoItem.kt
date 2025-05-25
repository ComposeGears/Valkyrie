package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.ui.util.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun InfoItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    CenterVerticalRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        InfoItem(
            modifier = Modifier.weight(1f),
            title = title,
            description = description,
        )
        content()
    }
}

@Composable
fun InfoItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.labelSmall,
            color = LocalContentColor.current.dim(),
        )
    }
}

@Preview
@Composable
private fun InfoItemPreview() = PreviewTheme {
    Column(modifier = Modifier.fillMaxWidth(0.7f)) {
        InfoItem(
            title = "Title",
            description = "Description",
        )
        InfoItem(
            title = "Title",
            description = "Description",
            content = {
                Checkbox(
                    checked = true,
                    onCheckedChange = {},
                )
            },
        )
    }
}
