package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.applyIf
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.rememberMutableState
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.ArrowDown
import io.github.composegears.valkyrie.compose.util.isLight
import io.github.composegears.valkyrie.compose.util.subtle
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun DropdownMenu(
    current: String,
    values: List<String>,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit,
) {
    val density = LocalDensity.current
    var dropdownVisible by rememberMutableState { false }

    Box(modifier = modifier) {
        var minDropDownWidth by rememberMutableState { 0.dp }

        Column {
            CenterVerticalRow(
                modifier = Modifier
                    .onSizeChanged {
                        minDropDownWidth = with(density) { it.width.toDp() }
                    }
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.subtle(),
                        shape = CircleShape,
                    )
                    .clickable { dropdownVisible = true }
                    .padding(start = 12.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                    .animateContentSize(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val rotation by animateFloatAsState(if (dropdownVisible) -180f else 0f)
                Text(
                    text = current,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
                Icon(
                    modifier = Modifier.graphicsLayer {
                        rotationZ = rotation
                    },
                    imageVector = ValkyrieIcons.Outlined.ArrowDown,
                    contentDescription = null,
                )
            }
        }

        DropdownMenu(
            modifier = Modifier
                .widthIn(min = minDropDownWidth)
                .applyIf(!MaterialTheme.colorScheme.isLight) {
                    border(
                        width = Dp.Hairline,
                        color = MaterialTheme.colorScheme.onSurface.subtle(),
                        shape = MaterialTheme.shapes.extraSmall,
                    )
                },
            expanded = dropdownVisible,
            onDismissRequest = { dropdownVisible = false },
            containerColor = MaterialTheme.colorScheme.surface,
        ) {
            values.forEach {
                DropdownMenuItem(
                    modifier = Modifier,
                    text = it,
                    selected = it == current,
                    onClick = {
                        dropdownVisible = false
                        onSelect(it)
                    },
                )
            }
        }
    }
}

@Composable
private fun DropdownMenuItem(
    text: String,
    onClick: () -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(
                color = when {
                    selected -> MaterialTheme.colorScheme.primary.subtle()
                    else -> Color.Unspecified
                },
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun DropdownMenuPreview() = PreviewTheme(isDark = true) {
    var current by rememberMutableState { "Item 1" }
    DropdownMenu(
        current = current,
        values = listOf("Item 1", "Item 2", "Long Item Name"),
        onSelect = { current = it },
    )
}
