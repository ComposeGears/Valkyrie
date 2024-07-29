package io.github.composegears.valkyrie.ui.screen.settings.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyDelegateProperty
import io.github.composegears.valkyrie.ui.foundation.HighlightColors
import io.github.composegears.valkyrie.ui.foundation.Tooltip
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.foundation.getHighlightedCode
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.foundation.theme.isLight
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.ui.model.SettingsAction.UpdateOutputFormat

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
        Text(text = "Output format", style = MaterialTheme.typography.bodyMedium)
        VerticalSpacer(8.dp)
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            val highlightColors = if (MaterialTheme.colorScheme.isLight) {
                HighlightColors.LIGHT
            } else {
                HighlightColors.DARK
            }
            SelectableCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                text = "Backing property",
                hint = getHighlightedCode(
                    code = backingPropertyFormat,
                    colors = highlightColors,
                ),
                isSelected = outputFormat == BackingProperty,
                onSelect = { onAction(UpdateOutputFormat(BackingProperty)) },
            )
            SelectableCard(
                modifier = Modifier.weight(1f).fillMaxHeight(),
                text = "Lazy delegate property",
                hint = getHighlightedCode(
                    code = lazyPropertyFormat,
                    colors = highlightColors,
                ),
                isSelected = outputFormat == LazyDelegateProperty,
                onSelect = { onAction(UpdateOutputFormat(LazyDelegateProperty)) },
            )
        }
    }
}

@Composable
private fun SelectableCard(
    text: String,
    hint: AnnotatedString,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onSelect: () -> Unit,
) {
    Card(
        modifier = modifier,
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
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                text = text,
                style = MaterialTheme.typography.bodySmall,
            )
            Tooltip(
                modifier = Modifier.padding(end = 16.dp),
                text = hint,
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
    val ArrowLeft by lazy(NONE) {
        ImageVector.Builder(...).build()
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
