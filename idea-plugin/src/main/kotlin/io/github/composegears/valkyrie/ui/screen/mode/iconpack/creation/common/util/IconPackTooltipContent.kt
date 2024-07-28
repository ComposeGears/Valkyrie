package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.common.util

import androidx.compose.ui.text.AnnotatedString
import io.github.composegears.valkyrie.ui.util.codeBlockAnnotatedString

fun buildPackPackageHint(packageName: String, iconPackName: String): AnnotatedString {
    val packagePlaceholder = packageName.ifEmpty { "your.package" }
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

    return codeBlockAnnotatedString(
        codeBlock = codeBlock,
        highlightText = packagePlaceholder,
    )
}

fun buildIconPackHint(iconPackName: String): AnnotatedString {
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

    return codeBlockAnnotatedString(
        codeBlock = codeBlock,
        highlightText = iconPackPlaceholder,
    )
}
