package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.TooltipDefaults.rememberPlainTooltipPositionProvider
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.Help
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipButton(text: String, modifier: Modifier = Modifier) {
    TooltipBox(
        positionProvider = rememberPlainTooltipPositionProvider(),
        tooltip = {
            RichTooltip(
                text = {
                    Text(
                        modifier = modifier.padding(vertical = 8.dp),
                        text = text,
                        style = MaterialTheme.typography.bodySmall
                    )
                },
                colors = TooltipDefaults.richTooltipColors().copy(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                )
            )
        },
        state = rememberTooltipState(isPersistent = true)
    ) {
        Button(
            modifier = Modifier.size(36.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            ),
            shape = CircleShape,
            onClick = {},
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = ValkyrieIcons.Help,
                contentDescription = null,
            )
        }
    }
}