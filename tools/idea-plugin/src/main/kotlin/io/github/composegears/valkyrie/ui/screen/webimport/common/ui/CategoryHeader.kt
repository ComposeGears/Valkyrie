package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun CategoryHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(start = 4.dp),
        text = title,
        style = JewelTheme.typography.h4TextStyle,
    )
}

@Preview
@Composable
private fun CategoryHeaderPreview() = PreviewTheme {
    CategoryHeader(title = "Actions")
}
