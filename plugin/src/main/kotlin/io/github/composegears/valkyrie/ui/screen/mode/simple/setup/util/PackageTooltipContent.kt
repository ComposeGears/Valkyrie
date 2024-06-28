package io.github.composegears.valkyrie.ui.screen.mode.simple.setup.util

import androidx.compose.ui.text.AnnotatedString
import io.github.composegears.valkyrie.ui.util.codeBlockAnnotatedString

fun buildPackageHint(packageName: String): AnnotatedString {
    val packagePlaceholder = packageName.ifEmpty { "your.package" }
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

    return codeBlockAnnotatedString(
        codeBlock = codeBlock,
        highlightText = packagePlaceholder
    )
}
