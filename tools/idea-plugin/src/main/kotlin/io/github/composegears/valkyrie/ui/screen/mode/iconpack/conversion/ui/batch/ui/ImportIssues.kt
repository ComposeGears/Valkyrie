package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.batch.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconName
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ValidationError
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.util.BatchIconsValidator.toMessageText
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography

@OptIn(ExperimentalFoundationApi::class)
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
                text = stringResource("iconpack.conversion.import.issues.title"),
                style = JewelTheme.typography.h4TextStyle,
            )
            SelectionContainer {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = importIssues.toMessageText(),
                )
            }
            Spacer(16.dp)
            CenterVerticalRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
            ) {
                DefaultButton(onClick = onResolveIssues) {
                    Text(text = stringResource("iconpack.conversion.import.issues.auto.resolve"))
                }
                Tooltip(
                    tooltip = {
                        Text(
                            text = """
                                • Replace empty icon name with default "IconName"
                                • Remove spaces from icon name
                                • Remove broken icons from the conversion list
                                • Add incremental number suffix for duplicates
                            """.trimIndent(),
                        )
                    },
                ) {
                    Icon(
                        key = AllIconsKeys.General.ContextHelp,
                        contentDescription = stringResource("accessibility.help"),
                    )
                }
            }
        }
        HorizontalDivider()
    }
}

@Preview
@Composable
private fun ImportIssuesUIPreview() = PreviewTheme {
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
