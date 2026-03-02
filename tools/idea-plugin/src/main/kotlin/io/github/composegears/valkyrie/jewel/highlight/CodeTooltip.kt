package io.github.composegears.valkyrie.jewel.highlight

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import io.github.composegears.valkyrie.jewel.EditorText
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeTooltip(
    code: HighlightedCode,
    content: @Composable () -> Unit,
) {
    Tooltip(
        tooltip = {
            EditorText(code = code)
        },
        content = content,
    )
}

@Preview
@Composable
private fun CodeTooltipPreview() = ProjectPreviewTheme(alignment = Alignment.Center) {
    CodeTooltip(
        code = rememberCodeHighlight(text = "val t: Int = 0"),
        content = {
            Icon(
                key = AllIconsKeys.General.ContextHelp,
                contentDescription = stringResource("accessibility.help"),
            )
        },
    )
}
