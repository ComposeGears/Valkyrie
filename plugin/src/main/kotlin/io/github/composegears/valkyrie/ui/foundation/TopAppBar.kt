package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.ContentCopy
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons

@Composable
fun TopAppBar(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
fun BackAction(onBack: () -> Unit) {
    IconButton(onClick = onBack) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
            contentDescription = null
        )
    }
}

@Composable
fun AppBarTitle(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = 4.dp),
        text = title,
        style = MaterialTheme.typography.titleSmall
    )
}

@Composable
fun ClearAction(onClear: () -> Unit) {
    IconButton(onClick = onClear) {
        Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = null
        )
    }
}

@Composable
fun CopyAction(onCopy: () -> Unit) {
    IconButton(onClick = onCopy) {
        Icon(
            modifier = Modifier.size(18.dp),
            imageVector = ValkyrieIcons.ContentCopy,
            contentDescription = null
        )
    }
}

@Composable
fun SettingsAction(openSettings: () -> Unit) {
    IconButton(onClick = openSettings) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun TopAppBarPreview() {
    TopAppBar {
        BackAction {}
        AppBarTitle("Title")
        ClearAction {}
        CopyAction {}
        SettingsAction {}
    }
}