package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.jewel.highlight.CodeTooltip
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.highlights.core.getEmphasisLocations
import io.github.composegears.valkyrie.sdk.compose.highlights.core.rememberCodeHighlight
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun CodeTooltipHeader(
    text: String,
    highlights: Highlights,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(modifier = modifier) {
        Text(text)
        Spacer(8.dp)
        CodeTooltip(
            highlights = highlights,
            content = {
                Icon(
                    key = AllIconsKeys.General.ContextHelp,
                    contentDescription = stringResource("accessibility.help"),
                )
            },
        )
    }
}

@Composable
fun buildPackPackageHighlight(
    packageName: String,
    iconPackName: String,
): Highlights {
    val packagePlaceholder = packageName.ifEmpty { "com.test.iconpack" }
    val iconPackPlaceholder = iconPackName.ifEmpty { "YourPackName" }

    val codeBlock = """
        package $packagePlaceholder

        val $iconPackPlaceholder.MyIcon: ImageVector
            get() {
                if (_MyIcon != null) {
                    return _MyIcon!!
                }
            }
            ...
    """.trimIndent()

    return rememberCodeHighlight(
        codeBlock = codeBlock,
        emphasisLocation = getEmphasisLocations(
            codeBlock = codeBlock,
            highlightText = packagePlaceholder,
        ),
        isDark = JewelTheme.isDark,
    )
}

@Composable
fun buildIconPackHighlight(iconPackName: String): Highlights {
    val iconPackPlaceholder = iconPackName.ifEmpty { "YourPackName" }
    val codeBlock = """
        object $iconPackPlaceholder

        val $iconPackPlaceholder.MyIcon: ImageVector
            get() {
                if (_MyIcon != null) {
                    return _MyIcon!!
                }
            }
            ...
    """.trimIndent()

    return rememberCodeHighlight(
        codeBlock = codeBlock,
        emphasisLocation = getEmphasisLocations(
            codeBlock = codeBlock,
            highlightText = iconPackPlaceholder,
        ),
        isDark = JewelTheme.isDark,
    )
}

@Preview
@Composable
private fun CodeTooltipHeaderPreview() = PreviewTheme(alignment = Alignment.Center) {
    Row {
        CodeTooltipHeader(
            text = "Package",
            highlights = buildPackPackageHighlight(
                packageName = "com.example.iconpack",
                iconPackName = "MyIconPack",
            ),
        )
        Spacer(16.dp)
        CodeTooltipHeader(
            text = "IconPack",
            highlights = buildIconPackHighlight(
                iconPackName = "MyIconPack",
            ),
        )
    }
}
