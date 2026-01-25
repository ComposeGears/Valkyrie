package io.github.composegears.valkyrie.jewel.highlight

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.highlights.core.rememberCodeHighlight
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Tooltip
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CodeTooltip(
    highlights: Highlights,
    content: @Composable () -> Unit,
) {
    Tooltip(
        tooltip = {
            KtCodeViewer(highlights = highlights)
        },
        content = content,
    )
}

@Preview
@Composable
private fun CodeTooltipPreview() = PreviewTheme {
    CodeTooltip(
        highlights = rememberCodeHighlight(codeBlock = "", isDark = JewelTheme.isDark),
        content = {
            Icon(
                key = AllIconsKeys.General.ContextHelp,
                contentDescription = stringResource("accessibility.help"),
            )
        },
    )
}
