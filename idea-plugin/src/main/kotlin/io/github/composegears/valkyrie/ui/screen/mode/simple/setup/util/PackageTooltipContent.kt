package io.github.composegears.valkyrie.ui.screen.mode.simple.setup.util

import androidx.compose.runtime.Composable
import dev.snipme.highlights.Highlights
import io.github.composegears.valkyrie.ui.foundation.highlights.core.getEmphasisLocations
import io.github.composegears.valkyrie.ui.foundation.highlights.core.rememberCodeHighlight

@Composable
fun buildPackageHighlight(packageName: String): Highlights {
    val packagePlaceholder = packageName.ifEmpty { "com.test.iconpack" }
    val codeBlock = """
        package $packagePlaceholder

        val MyIcon: ImageVector
            get() {
                if (_MyIcon != null) {
                return _MyIcon!!
            }
            ...
        }
    """.trimIndent()

    return rememberCodeHighlight(
        codeBlock = codeBlock,
        emphasisLocation = getEmphasisLocations(
            codeBlock = codeBlock,
            highlightText = packagePlaceholder,
        ),
    )
}
