package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.HorizontalSpacer
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Search
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.compose.util.subtle
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun SearchInputField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.small,
    colors: SearchInputFieldColor = SearchInputFieldDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    CenterVerticalRow(
        modifier = modifier
            .width(IntrinsicSize.Max)
            .border(
                width = 1.dp,
                color = colors.borderColor,
                shape = shape,
            )
            .heightIn(min = 32.dp)
            .padding(start = 4.dp, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        HorizontalSpacer(4.dp)
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = value,
            textStyle = MaterialTheme.typography.bodySmall.copy(color = colors.textColor),
            cursorBrush = SolidColor(colors.cursorColor),
            onValueChange = onValueChange,
            singleLine = true,
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    innerTextField()
                    if (value.isEmpty()) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(color = colors.textColor),
                            maxLines = 1,
                        )
                    }
                }
            },
        )
        AnimatedContent(
            targetState = value.isEmpty(),
            transitionSpec = {
                fadeIn(tween(220)) togetherWith fadeOut(tween(90))
            },
        ) {
            if (it) {
                CompositionLocalProvider(LocalContentColor provides colors.leadingColor) {
                    leadingIcon?.invoke()
                }
            } else {
                CompositionLocalProvider(LocalContentColor provides colors.trailingColor) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable(
                                onClick = {
                                    onValueChange("")
                                },
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        trailingIcon?.invoke()
                    }
                }
            }
        }
    }
}

object SearchInputFieldDefaults {
    @Composable
    fun colors(): SearchInputFieldColor {
        return SearchInputFieldColor(
            leadingColor = MaterialTheme.colorScheme.onSurface.dim(),
            trailingColor = MaterialTheme.colorScheme.onSurface.dim(),
            textColor = MaterialTheme.colorScheme.onSurfaceVariant.dim(),
            borderColor = MaterialTheme.colorScheme.onSurface.subtle(),
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

data class SearchInputFieldColor(
    val leadingColor: Color,
    val trailingColor: Color,
    val textColor: Color,
    val cursorColor: Color,
    val borderColor: Color,
)

@Preview
@Composable
private fun SearchInputFieldPreview() = PreviewTheme(isDark = false) {
    var text by rememberMutableState { "" }

    SearchInputField(
        modifier = Modifier.width(200.dp),
        value = text,
        label = "Search icons",
        onValueChange = { text = it },
        leadingIcon = {
            Icon(
                imageVector = ValkyrieIcons.Outlined.Search,
                contentDescription = null,
            )
        },
        trailingIcon = {
            Icon(
                imageVector = ValkyrieIcons.Close,
                contentDescription = null,
            )
        },
    )
}
