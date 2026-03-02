package io.github.composegears.valkyrie.ui.screen.mode.iconpack.common

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.highlight.CodeTooltip
import io.github.composegears.valkyrie.jewel.highlight.HighlightedCode
import io.github.composegears.valkyrie.jewel.highlight.rememberCodeHighlight
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun CodeTooltipHeader(
    text: String,
    code: HighlightedCode,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(modifier = modifier) {
        Text(text)
        Spacer(8.dp)
        CodeTooltip(
            code = code,
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
): HighlightedCode {
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
        text = codeBlock,
        style = {
            underline(packagePlaceholder)
        },
    )
}

@Composable
fun buildIconPackHighlight(iconPackName: String): HighlightedCode {
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
        text = codeBlock,
        style = {
            underline(iconPackPlaceholder)
        },
    )
}

@Preview
@Composable
private fun CodeTooltipHeaderPreview() = ProjectPreviewTheme(alignment = Alignment.Center) {
    Row {
        CodeTooltipHeader(
            text = "Package",
            code = buildPackPackageHighlight(
                packageName = "com.example.iconpack",
                iconPackName = "MyIconPack",
            ),
        )
        Spacer(16.dp)
        CodeTooltipHeader(
            text = "IconPack",
            code = buildIconPackHighlight(
                iconPackName = "MyIconPack",
            ),
        )
    }
}
