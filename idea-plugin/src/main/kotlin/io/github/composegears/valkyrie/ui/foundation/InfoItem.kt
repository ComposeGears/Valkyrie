package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

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
private fun PreviewInfoItem() = PreviewTheme {
    InfoItem(
        title = "Title",
        description = "Description",
    )
}
