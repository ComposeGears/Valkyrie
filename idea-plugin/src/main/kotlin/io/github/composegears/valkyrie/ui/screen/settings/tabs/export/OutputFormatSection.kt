package io.github.composegears.valkyrie.ui.screen.settings.tabs.export

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyProperty
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.highlights.core.rememberCodeHighlight
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateOutputFormat
import io.github.composegears.valkyrie.ui.screen.settings.tabs.export.ui.SelectableCard

@Composable
fun OutputFormatSection(
    outputFormat: OutputFormat,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(horizontal = 24.dp),
    onAction: (SettingsAction) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
    ) {
        Text(
            text = "Output format",
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        VerticalSpacer(8.dp)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SelectableCard(
                modifier = Modifier.weight(1f),
                text = "Backing property",
                highlights = rememberCodeHighlight(backingPropertyFormat),
                isSelected = outputFormat == BackingProperty,
                onSelect = { onAction(UpdateOutputFormat(BackingProperty)) },
            )
            SelectableCard(
                modifier = Modifier.weight(1f),
                text = "Lazy delegate property",
                highlights = rememberCodeHighlight(lazyPropertyFormat),
                isSelected = outputFormat == LazyProperty,
                onSelect = { onAction(UpdateOutputFormat(LazyProperty)) },
            )
        }
    }
}

private val backingPropertyFormat = """
    val ArrowLeft: ImageVector
        get() {
            if (_ArrowLeft != null) {
                return _ArrowLeft!!
            }
            _ArrowLeft = ImageVector.Builder(...)
                .build()

            return _ArrowLeft!!
        }

    private var _ArrowLeft: ImageVector? = null
""".trimIndent()

private val lazyPropertyFormat = """
    val ArrowLeft by lazy(LazyThreadSafetyMode.NONE) {
        ImageVector.Builder(...)
            .build()
    }
""".trimIndent()

@Preview
@Composable
private fun OutputFormatSectionPreview() = PreviewTheme {
    OutputFormatSection(
        outputFormat = BackingProperty,
        onAction = {},
    )
}
