package io.github.composegears.valkyrie.jewel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.TooltipHintToggleButton
import io.github.composegears.valkyrie.jewel.button.TooltipIconButton
import io.github.composegears.valkyrie.jewel.button.TooltipToggleButton
import io.github.composegears.valkyrie.jewel.icons.BlackCircle
import io.github.composegears.valkyrie.jewel.icons.BlackCircleDark
import io.github.composegears.valkyrie.jewel.icons.Chessboard
import io.github.composegears.valkyrie.jewel.icons.ChessboardDark
import io.github.composegears.valkyrie.jewel.icons.IntelliJIcons
import io.github.composegears.valkyrie.jewel.icons.WhiteCircle
import io.github.composegears.valkyrie.jewel.icons.WhiteCircleDark
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.typography

@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    CenterVerticalRow(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = content,
    )
}

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier.padding(start = 4.dp, end = 8.dp),
        text = text,
        maxLines = 1,
        style = JewelTheme.typography.labelTextStyle,
    )
}

@Composable
fun BackAction(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.Actions.Play_back,
        contentDescription = stringResource("accessibility.back"),
        onClick = onBack,
        tooltipText = stringResource("component.toolbar.back.tooltip"),
    )
}

@Composable
fun CloseAction(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.General.Close,
        contentDescription = stringResource("accessibility.close"),
        onClick = onClose,
        tooltipText = stringResource("component.toolbar.close.tooltip"),
    )
}

@Composable
fun CopyAction(
    modifier: Modifier = Modifier,
    onCopy: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.General.Copy,
        contentDescription = stringResource("accessibility.copy"),
        onClick = onCopy,
        tooltipText = stringResource("component.toolbar.copy.tooltip"),
    )
}

@Composable
fun SettingsAction(
    modifier: Modifier = Modifier,
    openSettings: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.General.Settings,
        contentDescription = stringResource("accessibility.settings"),
        onClick = openSettings,
        tooltipText = stringResource("component.toolbar.settings.tooltip"),
    )
}

@Composable
fun ChessboardAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        imageVector = when {
            JewelTheme.isDark -> IntelliJIcons.ChessboardDark
            else -> IntelliJIcons.Chessboard
        },
        contentDescription = stringResource("accessibility.chessboard"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.chessboard.tooltip"),
    )
}

@Composable
fun WhiteCircleAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        imageVector = when {
            JewelTheme.isDark -> IntelliJIcons.WhiteCircleDark
            else -> IntelliJIcons.WhiteCircle
        },
        contentDescription = stringResource("accessibility.background.white"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.white.background.tooltip"),
    )
}

@Composable
fun BlackCircleAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        imageVector = when {
            JewelTheme.isDark -> IntelliJIcons.BlackCircleDark
            else -> IntelliJIcons.BlackCircle
        },
        contentDescription = stringResource("accessibility.background.black"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.black.background.tooltip"),
    )
}

@Composable
fun ZoomInAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.Graph.ZoomIn,
        contentDescription = stringResource("accessibility.zoom.in"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.zoom.in"),
    )
}

@Composable
fun ZoomOutAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.Graph.ZoomOut,
        contentDescription = stringResource("accessibility.zoom.out"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.zoom.out"),
    )
}

@Composable
fun ActualZoomAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.Graph.ActualZoom,
        contentDescription = stringResource("accessibility.zoom.actual"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.zoom.actual"),
    )
}

@Composable
fun FitContentAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.General.FitContent,
        contentDescription = stringResource("accessibility.fit.content"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.fit.content.tooltip"),
    )
}

@Composable
fun PreviewAction(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TooltipIconButton(
        modifier = modifier,
        key = AllIconsKeys.Actions.Preview,
        contentDescription = stringResource("accessibility.preview"),
        onClick = onClick,
        tooltipText = stringResource("component.toolbar.preview.tooltip"),
    )
}

@Composable
fun EditToggleAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onEdit: (Boolean) -> Unit,
) {
    TooltipToggleButton(
        modifier = modifier,
        key = AllIconsKeys.Actions.Edit,
        contentDescription = stringResource("accessibility.edit"),
        value = selected,
        onValueChange = onEdit,
        tooltipText = stringResource("component.toolbar.edit.tooltip"),
    )
}

@Composable
fun PreviewToggleAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onPreview: (Boolean) -> Unit,
) {
    TooltipToggleButton(
        modifier = modifier,
        key = AllIconsKeys.Actions.Preview,
        contentDescription = stringResource("accessibility.preview"),
        value = selected,
        onValueChange = onPreview,
        tooltipText = stringResource("component.toolbar.preview.tooltip"),
    )
}

@Composable
fun NotificationToggleAction(
    selected: Boolean,
    modifier: Modifier = Modifier,
    onNotification: (Boolean) -> Unit,
) {
    TooltipHintToggleButton(
        modifier = modifier,
        key = AllIconsKeys.Toolwindows.Notifications,
        contentDescription = stringResource("accessibility.notifications"),
        value = selected,
        onValueChange = onNotification,
        tooltipText = stringResource("component.toolbar.notifications.tooltip"),
    )
}

@Preview
@Composable
private fun ToolbarPreview() = PreviewTheme {
    var isEdit by rememberMutableState { true }
    var isPreview by rememberMutableState { true }
    var isNotification by rememberMutableState { true }

    Column {
        Toolbar {
            BackAction {}
            Title(text = "Title")
            WeightSpacer()
            CloseAction {}
            CopyAction {}
            EditToggleAction(
                selected = isEdit,
                onEdit = { isEdit = it },
            )
            PreviewToggleAction(
                selected = isPreview,
                onPreview = { isPreview = it },
            )
            NotificationToggleAction(
                selected = isNotification,
                onNotification = { isNotification = it },
            )
            SettingsAction {}
        }
    }
}
