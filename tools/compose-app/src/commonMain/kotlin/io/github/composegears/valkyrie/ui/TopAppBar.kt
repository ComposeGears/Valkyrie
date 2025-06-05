package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Back
import io.github.composegears.valkyrie.compose.icons.outlined.Close

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    CenterVerticalRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}

@Composable
fun BackAction(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onBack,
    ) {
        Icon(
            imageVector = ValkyrieIcons.Outlined.Back,
            contentDescription = null,
        )
    }
}

@Composable
fun CloseAction(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClose,
    ) {
        Icon(
            imageVector = ValkyrieIcons.Outlined.Close,
            contentDescription = null,
        )
    }
}

@Composable
fun AppBarTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = title,
        maxLines = 1,
        style = MaterialTheme.typography.bodyMedium,
    )
}
