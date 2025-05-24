package io.github.composegears.valkyrie.ui.screen.settings.tabs.export

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.CenterVerticalRow
import io.github.composegears.valkyrie.ui.foundation.HorizontalSpacer
import io.github.composegears.valkyrie.ui.foundation.dim
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun IndentSizeSection(
    indent: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberMutableState { false }

    val rotateAnimation by animateFloatAsState(
        targetValue = when (expanded) {
            true -> -180f
            false -> 0f
        },
        animationSpec = tween(350),
    )
    ListItem(
        modifier = modifier
            .clickable { expanded = true }
            .padding(horizontal = 8.dp),
        headlineContent = {
            Text(text = "Indent size")
        },
        supportingContent = {
            Text(
                text = "Determines the number of spaces used for each level of indentation in the exported icon",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = LocalContentColor.current.dim(),
            )
        },
        trailingContent = {
            CenterVerticalRow(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { expanded = true }
                    .padding(8.dp),
            ) {
                AnimatedIndentText(indent)
                HorizontalSpacer(12.dp)
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotateAnimation
                    },
                )
            }
            IndentOptionMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                onClick = {
                    onValueChange(it)
                    expanded = false
                },
            )
        },
    )
}

@Composable
private fun AnimatedIndentText(indent: Int) {
    AnimatedContent(
        targetState = indent,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
    ) {
        Text(
            text = it.toString(),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun IndentOptionMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: (Int) -> Unit,
) {
    if (expanded) {
        DropdownMenu(
            expanded = true,
            onDismissRequest = onDismissRequest,
        ) {
            repeat(10) {
                val index = it + 1
                DropdownMenuItem(
                    text = {
                        Text(
                            text = index.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalContentColor.current.dim(),
                        )
                    },
                    onClick = { onClick(index) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun IndentSizeSectionPreview() = PreviewTheme {
    IndentSizeSection(
        indent = 4,
        onValueChange = { },
    )
}
