package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun SecondaryButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val color = when {
        enabled -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface.disabled()
    }

    OutlinedButton(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        border = BorderStroke(width = 0.6.dp, color = color),
    ) {
        content()
    }
}

@Preview
@Composable
private fun SecondaryButtonPreview() = PreviewTheme {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SecondaryButton(onClick = {}) {
            Text(text = "Secondary button")
        }
        SecondaryButton(
            onClick = {},
            enabled = false,
        ) {
            Text(text = "Disabled secondary button")
        }

    }
}
