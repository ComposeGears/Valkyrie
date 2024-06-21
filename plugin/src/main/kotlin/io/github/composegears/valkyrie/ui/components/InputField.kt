package io.github.composegears.valkyrie.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.icons.Help
import io.github.composegears.valkyrie.ui.icons.ValkyrieIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    caption: String,
    value: String,
    isError: Boolean = false,
    tooltipValue: AnnotatedString,
    supportingText: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 4.dp),
                text = caption,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            TooltipBox(
                positionProvider = TooltipDefaults.rememberRichTooltipPositionProvider(),
                tooltip = {
                    Surface(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Box(modifier = Modifier.padding(PaddingValues(8.dp, 4.dp))) {
                            Text(text = tooltipValue, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                },
                state = rememberTooltipState(isPersistent = true)
            ) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = ValkyrieIcons.Help,
                    contentDescription = null,
                )
            }
        }
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                cursorColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            isError = isError,
            singleLine = true,
            supportingText = supportingText,
            trailingIcon = {
                if (value.isNotEmpty()) {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}
