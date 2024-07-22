package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.ContentCopy
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TopAppBar(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun BackAction(onBack: () -> Unit) {
    IconButton(
        onClick = onBack,
        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    )
}

@Composable
fun AppBarTitle(title: String) {
    Text(
        modifier = Modifier.padding(horizontal = 8.dp),
        text = title,
        maxLines = 1,
        style = MaterialTheme.typography.titleSmall,
    )
}

@Composable
fun ClearAction(onClear: () -> Unit) {
    IconButton(
        imageVector = Icons.Default.Clear,
        onClick = onClear,
    )
}

@Composable
fun CopyAction(onCopy: () -> Unit) {
    IconButton(
        imageVector = ValkyrieIcons.ContentCopy,
        onClick = onCopy,
        iconSize = 18.dp,
    )
}

@Composable
fun SettingsAction(openSettings: () -> Unit) {
    IconButton(
        imageVector = Icons.Default.Settings,
        onClick = openSettings,
    )
}

@Preview
@Composable
private fun TopAppBarPreview() = PreviewTheme {
    Box(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction {}
            AppBarTitle(title = "Page title")
            WeightSpacer()
            ClearAction {}
            CopyAction {}
            SettingsAction {}
        }
    }
}
