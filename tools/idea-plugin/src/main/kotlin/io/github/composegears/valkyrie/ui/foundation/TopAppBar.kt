package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.idea.Notifications
import io.github.composegears.valkyrie.compose.icons.idea.Settings
import io.github.composegears.valkyrie.ui.foundation.icons.Back
import io.github.composegears.valkyrie.ui.foundation.icons.Close
import io.github.composegears.valkyrie.ui.foundation.icons.Copy
import io.github.composegears.valkyrie.ui.foundation.icons.Edit
import io.github.composegears.valkyrie.ui.foundation.icons.Watch
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    CenterVerticalRow(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
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
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Back,
        iconSize = 18.dp,
        hint = "Back",
        onClick = onBack,
        selected = false,
    )
}

@Composable
fun CloseAction(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Close,
        hint = "Close",
        onClick = onClose,
        selected = false,
    )
}

@Composable
fun CopyAction(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit,
) {
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Copy,
        hint = "Copy",
        iconSize = 18.dp,
        onClick = onCopy,
        selected = false,
    )
}

@Composable
fun SettingsAction(
    modifier: Modifier = Modifier,
    openSettings: () -> Unit,
) {
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Idea.Settings,
        hint = "Settings",
        iconSize = 18.dp,
        onClick = openSettings,
        selected = false,
    )
}

@Composable
fun PreviewAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onPreview: () -> Unit,
) {
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Watch,
        hint = "Preview",
        iconSize = 18.dp,
        onClick = onPreview,
        selected = selected,
    )
}

@Composable
fun EditAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
) {
    SelectableAction(
        modifier = modifier.size(32.dp),
        imageVector = ValkyrieIcons.Edit,
        hint = "Edit",
        iconSize = 18.dp,
        onClick = onEdit,
        selected = selected,
    )
}

@Composable
fun NotificationAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onNotification: () -> Unit,
) {
    val errorColor = MaterialTheme.colorScheme.error

    SelectableAction(
        modifier = modifier
            .size(32.dp)
            .drawBehind {
                drawCircle(
                    color = errorColor,
                    radius = 6f,
                    center = Offset(size.width - 8.dp.toPx(), 6.dp.toPx()),
                )
            },
        imageVector = ValkyrieIcons.Idea.Notifications,
        hint = "Notifications",
        iconSize = 18.dp,
        onClick = onNotification,
        selected = selected,
    )
}

@Composable
private fun SelectableAction(
    imageVector: ImageVector,
    hint: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    iconSize: Dp = Dp.Unspecified,
    onClick: () -> Unit,
) {
    TooltipButton(
        hint = hint,
        modifier = modifier,
    ) {
        IconButton(
            imageVector = imageVector,
            iconSize = iconSize,
            onClick = onClick,
            colors = if (selected) {
                IconButtonDefaults.iconButtonColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                IconButtonDefaults.iconButtonColors()
            },
        )
    }
}

@Preview
@Composable
private fun TopAppBarPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    Column {
        TopAppBar {
            BackAction {}
            AppBarTitle(title = "Title")
            WeightSpacer()
            CloseAction {}
            CopyAction {}
            PreviewAction(selected = false) { }
            EditAction(selected = false) {}
            NotificationAction(selected = false) { }
            SettingsAction {}
        }
        HorizontalDivider()
        TopAppBar {
            BackAction {}
            AppBarTitle(title = "Title")
            WeightSpacer()
            CloseAction {}
            CopyAction {}
            EditAction(selected = true) {}
            PreviewAction(selected = true) { }
            NotificationAction(selected = true) { }
            SettingsAction {}
        }
    }
}
