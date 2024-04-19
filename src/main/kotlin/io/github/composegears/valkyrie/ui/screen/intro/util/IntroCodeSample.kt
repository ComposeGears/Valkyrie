package io.github.composegears.valkyrie.ui.screen.intro.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

private fun getIconPackCodeBlock(pack: String) = """val $pack.MyIcon: ImageVector
    get() {
        if (_MyIcon != null) {
            return _MyIcon!!
        }
    }
    ..."""

private fun getPackageCodeBlock(packageName: String, iconPackName: String) = """
package $packageName

val $iconPackName.MyIcon: ImageVector
    get() {
        if (_MyIcon != null) {
            return _MyIcon!!
        }
    }
    ...""".trimIndent()

fun getIconPackAnnotatedString(iconPackName: String): AnnotatedString {
    val codeBlock = getIconPackCodeBlock(iconPackName)

    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
            append(codeBlock)
        }
        val startIndex = codeBlock.indexOf(iconPackName)
        val lastIndex = startIndex + iconPackName.length
        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), startIndex, lastIndex)
    }
}

fun getPackageAnnotatedString(packageName: String, iconPackName: String): AnnotatedString {
    val codeBlock = getPackageCodeBlock(packageName, iconPackName)

    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.ExtraLight)) {
            append(codeBlock)
        }
        val startIndex = codeBlock.indexOf(packageName)
        val lastIndex = startIndex + packageName.length
        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), startIndex, lastIndex)
    }
}