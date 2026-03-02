package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

private const val INLINE_CONTENT_ID = "inlineContentIcon"

@Composable
fun InlineIconText(
    text: String,
    key: IconKey,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified,
    style: TextStyle = JewelTheme.defaultTextStyle,
    iconSize: TextUnit = style.fontSize,
    textAlign: TextAlign = TextAlign.Unspecified,
    alternateText: String = "[icon]",
) {
    val parts = text.split(alternateText)

    val annotatedString = buildAnnotatedString {
        for (i in parts.indices) {
            append(parts[i])
            if (i < parts.lastIndex) {
                appendInlineContent(id = INLINE_CONTENT_ID, alternateText = alternateText)
            }
        }
    }

    val inlineContent = mapOf(
        INLINE_CONTENT_ID to InlineTextContent(
            placeholder = Placeholder(
                width = iconSize,
                height = iconSize,
                placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
            ),
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                key = key,
                contentDescription = null,
                colorFilter = when {
                    iconTint == Color.Unspecified -> null
                    else -> ColorFilter.tint(iconTint)
                },
            )
        },
    )

    Text(
        modifier = modifier,
        text = annotatedString,
        inlineContent = inlineContent,
        style = style,
        textAlign = textAlign,
    )
}

@Preview
@Composable
private fun InlineIconTextPreview() = PreviewTheme(alignment = Alignment.Center) {
    Column {
        InlineIconText(
            text = "This is a text with a [icon] in between",
            key = AllIconsKeys.Language.Kotlin,
            style = TextStyle.Default.copy(fontSize = 34.sp),
        )
        InlineIconText(
            text = "This is a text with a [icon] in between",
            key = AllIconsKeys.Language.Kotlin,
        )
    }
}
