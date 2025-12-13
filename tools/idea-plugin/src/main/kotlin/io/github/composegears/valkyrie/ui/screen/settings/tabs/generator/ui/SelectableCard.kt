package io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.compose.codeviewer.core.rememberCodeHighlight
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.ui.foundation.highlights.CodeViewerTooltip
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun SelectableCard(
    text: String,
    highlights: Highlights,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
) {
    Card(
        modifier = modifier.requiredHeight(72.dp),
        onClick = onSelect,
        border = if (isSelected) {
            BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
            )
        } else {
            null
        },
    ) {
        CenterVerticalRow(modifier = Modifier.fillMaxHeight()) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                text = text,
                style = MaterialTheme.typography.bodySmall,
            )
            CodeViewerTooltip(
                modifier = Modifier.padding(end = 16.dp),
                highlights = highlights,
            )
        }
    }
}

@Preview
@Composable
private fun SelectableCardPreview() = PreviewTheme {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SelectableCard(
            modifier = Modifier.width(300.dp),
            text = "Selected Card",
            highlights = rememberCodeHighlight(
                """
                val MyIcon = ImageVector.Builder()
                    .width(24.dp)
                    .height(24.dp)
                    .build()
                """.trimIndent(),
            ),
            isSelected = true,
            onSelect = {},
        )
        SelectableCard(
            modifier = Modifier.width(300.dp),
            text = "Unselected Card",
            highlights = rememberCodeHighlight(
                """
                val MyIcon by lazy {
                    ImageVector.Builder()
                        .width(24.dp)
                        .height(24.dp)
                        .build()
                }
                """.trimIndent(),
            ),
            isSelected = false,
            onSelect = {},
        )
    }
}
