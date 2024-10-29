package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.ui.foundation.icons.Back
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.icons.Copy
import io.github.composegears.valkyrie.ui.foundation.icons.Settings
import io.github.composegears.valkyrie.ui.foundation.icons.ValkyrieIcons
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content()
    }
}

@Composable
fun AppBarTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(start = 8.dp, end = 8.dp, bottom = 4.dp),
        text = title,
        maxLines = 1,
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
fun BackAction(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(32.dp),
        onClick = onBack,
        iconSize = 18.dp,
        imageVector = ValkyrieIcons.Back,
    )
}

@Composable
fun ClearAction(
    modifier: Modifier = Modifier,
    onClear: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Close,
        onClick = onClear,
    )
}

@Composable
fun CopyAction(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Copy,
        iconSize = 18.dp,
        onClick = onCopy,
    )
}

@Composable
fun SettingsAction(
    modifier: Modifier = Modifier,
    openSettings: () -> Unit,
) {
    IconButton(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Settings,
        iconSize = 18.dp,
        onClick = openSettings,
    )
}

@Preview
@Composable
private fun TopAppBarPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    TopAppBar {
        BackAction {}
        AppBarTitle(title = "Title")
        WeightSpacer()
        ClearAction {}
        CopyAction {}
        SettingsAction {}
    }
}
