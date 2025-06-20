package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util

import androidx.compose.runtime.Composable
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.compose.codeviewer.core.getEmphasisLocations
import io.github.composegears.valkyrie.compose.codeviewer.core.rememberCodeHighlight

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
    )
}
