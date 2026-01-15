package io.github.composegears.valkyrie.ui.screen.settings.tabs.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import io.github.composegears.valkyrie.jewel.settings.CheckboxSettingsRow
import io.github.composegears.valkyrie.jewel.settings.Group
import io.github.composegears.valkyrie.jewel.settings.GroupSpacing
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.domain.model.PreviewType
import io.github.composegears.valkyrie.ui.screen.settings.PreviewSettings
import io.github.composegears.valkyrie.ui.screen.settings.SettingsViewModel
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateIconsInProjectView
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewType
import io.github.composegears.valkyrie.ui.screen.settings.tabs.preview.ui.PreviewBgSection
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val ImageVectorPreviewSettingsScreen by navDestination {
    val viewModel = viewModel<SettingsViewModel>(viewModelStoreOwner = navController())
    val previewSettings by viewModel.previewSettings.collectAsState()

    ImageVectorPreviewSettingsUi(
        previewSettings = previewSettings,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ImageVectorPreviewSettingsUi(
    previewSettings: PreviewSettings,
    onAction: (SettingsAction) -> Unit,
) {
    VerticallyScrollableContainer {
        Column {
            Group(text = stringResource("settings.imagevector.preview.ide.preview.header")) {
                CheckboxSettingsRow(
                    text = stringResource("settings.imagevector.preview.ide.option.previewer"),
                    infoText = stringResource("settings.imagevector.preview.ide.option.previewer.description"),
                    checked = previewSettings.showImageVectorPreview,
                    onCheckedChange = { onAction(UpdateImageVectorPreview(it)) },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.imagevector.preview.ide.option.projectview"),
                    infoText = stringResource("settings.imagevector.preview.ide.option.projectview.description"),
                    checked = previewSettings.showIconsInProjectView,
                    onCheckedChange = { onAction(UpdateIconsInProjectView(it)) },
                )
            }
            GroupSpacing()
            PreviewBgSection(
                previewType = previewSettings.previewType,
                onSelect = { onAction(UpdatePreviewType(it)) },
            )
            GroupSpacing()
        }
    }
}

@Preview
@Composable
private fun ImageVectorPreviewSettingsPreview() = PreviewTheme(alignment = Alignment.TopStart) {
    var showImageVectorPreview by rememberMutableState { true }
    var showIconsInProjectView by rememberMutableState { true }
    var previewType by rememberMutableState { PreviewType.Auto }
    ImageVectorPreviewSettingsUi(
        previewSettings = PreviewSettings(
            showImageVectorPreview = showImageVectorPreview,
            showIconsInProjectView = showIconsInProjectView,
            previewType = previewType,
        ),
        onAction = {
            when (it) {
                is UpdateImageVectorPreview -> showImageVectorPreview = it.enabled
                is UpdateIconsInProjectView -> showIconsInProjectView = it.enabled
                is UpdatePreviewType -> previewType = it.previewType
                else -> {}
            }
        },
    )
}
