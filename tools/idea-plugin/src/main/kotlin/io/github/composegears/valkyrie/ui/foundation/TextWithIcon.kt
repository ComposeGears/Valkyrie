package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import io.github.composegears.valkyrie.ui.foundation.icons.KotlinLogo
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

private const val INLINE_CONTENT_ID = "inlineContentIcon"

@Composable
fun TextWithIcon(
    text: String,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current,
    iconSize: TextUnit = style.fontSize,
    textAlign: TextAlign? = null,
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
            Image(
                imageVector = imageVector,
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
private fun TextWithIconPreview() = PreviewTheme {
    TextWithIcon(
        text = "This is a text with a [icon] in between.",
        imageVector = ValkyrieIcons.KotlinLogo,
    )
}
