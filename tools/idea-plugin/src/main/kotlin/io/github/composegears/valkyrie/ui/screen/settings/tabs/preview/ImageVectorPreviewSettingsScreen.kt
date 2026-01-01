package io.github.composegears.valkyrie.ui.screen.settings.tabs.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.settings.PreviewSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIconsInProjectView
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewType

val ImageVectorPreviewSettingsScreen by navDestination<Unit> {
    val viewModel = viewModel<SettingsViewModel>(navController())
    val previewSettings by viewModel.previewSettings.collectAsState()

    ImageVectorPreviewSettingsUi(
        previewSettings = previewSettings,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ImageVectorPreviewSettingsUi(
    previewSettings: PreviewSettings,
    modifier: Modifier = Modifier,
    onAction: (SettingsAction) -> Unit,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        VerticalSpacer(16.dp)
        ListItem(
            modifier = Modifier
                .toggleable(
                    value = previewSettings.showImageVectorPreview,
                    onValueChange = { onAction(UpdateImageVectorPreview(it)) },
                )
                .padding(horizontal = 8.dp)
                .heightIn(max = 100.dp),
            headlineContent = {
                Text(text = "Show ImageVector preview")
            },
            supportingContent = {
                Text(
                    text = "Enable icon preview functionality in the IDE without @Preview annotation",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                Switch(
                    modifier = Modifier.scale(0.9f),
                    checked = previewSettings.showImageVectorPreview,
                    onCheckedChange = { onAction(UpdateImageVectorPreview(it)) },
                )
            },
        )
        ListItem(
            modifier = Modifier
                .toggleable(
                    value = previewSettings.showIconsInProjectView,
                    onValueChange = { onAction(UpdateIconsInProjectView(it)) },
                )
                .padding(horizontal = 8.dp)
                .heightIn(max = 100.dp),
            headlineContent = {
                Text(text = "Show icons in Project View")
            },
            supportingContent = {
                Text(
                    text = "Display ImageVector preview as file icons in the project tree. \nTo ensure the icon cache updates fully, it's recommended to restart the IDE.",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = LocalContentColor.current.dim(),
                )
            },
            trailingContent = {
                Switch(
                    modifier = Modifier.scale(0.9f),
                    checked = previewSettings.showIconsInProjectView,
                    onCheckedChange = { onAction(UpdateIconsInProjectView(it)) },
                )
            },
        )
        VerticalSpacer(16.dp)
        PreviewBgSection(
            previewType = previewSettings.previewType,
            onSelect = { onAction(UpdatePreviewType(it)) },
        )
    }
}

@Preview
@Composable
private fun ImageVectorPreviewSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    ImageVectorPreviewSettingsUi(
        previewSettings = PreviewSettings(
            showImageVectorPreview = true,
            showIconsInProjectView = true,
            previewType = PreviewType.Auto,
        ),
        onAction = {},
    )
}
