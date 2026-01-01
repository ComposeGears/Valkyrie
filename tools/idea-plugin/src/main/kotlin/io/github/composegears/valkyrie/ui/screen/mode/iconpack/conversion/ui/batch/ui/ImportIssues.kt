package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.filled.Help
import io.github.composegears.valkyrie.compose.util.disabled
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider
import io.github.composegears.valkyrie.ui.foundation.TooltipIcon
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.toMessageText

@Composable
fun ImportIssuesUI(
    importIssues: Map<ValidationError, List<IconName>>,
    modifier: Modifier = Modifier,
    onResolveIssues: () -> Unit,
) {
    Column(modifier = modifier) {
        HorizontalDivider()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
                text = "Import issues:",
                style = MaterialTheme.typography.labelSmall,
                color = LocalContentColor.current.disabled(),
            )
            SelectionContainer {
                Text(
                    text = importIssues.toMessageText(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            VerticalSpacer(16.dp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
            ) {
                Button(onClick = onResolveIssues) {
                    Text(text = "Auto resolve issues")
                }
                TooltipIcon(
                    hint = """
                        • Replace empty icon name with default "IconName"
                        • Remove spaces from icon name
                        • Remove broken icons from the conversion list
                        • Add incremental number suffix for duplicates
                    """.trimIndent(),
                    icon = ValkyrieIcons.Filled.Help,
                )
            }
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun ImportIssuesUIPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImportIssuesUI(
        importIssues = mapOf(
            ValidationError.IconNameEmpty to listOf(IconName("")),
            ValidationError.IconNameContainsSpace to listOf(IconName("Ic Duplicate")),
            ValidationError.FailedToParseFile to listOf(IconName("test.svg")),
            ValidationError.HasDuplicates to listOf(IconName("IcDuplicate")),
        ),
        onResolveIssues = {},
    )
}
