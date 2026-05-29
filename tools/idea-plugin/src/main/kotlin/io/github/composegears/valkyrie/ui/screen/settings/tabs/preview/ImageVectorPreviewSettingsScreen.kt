package io.github.composegears.valkyrie.ui.screen.settings.tabs.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.intellij.openapi.application.ex.ApplicationManagerEx
import com.intellij.openapi.ui.MessageDialogBuilder
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
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorGutterIcon
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdateImageVectorPreview
import io.github.composegears.valkyrie.ui.screen.settings.model.SettingsAction.UpdatePreviewType
import io.github.composegears.valkyrie.ui.screen.settings.tabs.preview.ui.PreviewBgSection
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import java.awt.Component
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.LocalComponent
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val ImageVectorPreviewSettingsScreen by navDestination {
    val viewModel = viewModel<SettingsViewModel>(viewModelStoreOwner = navController())
    val previewSettings by viewModel.previewSettings.collectAsState()

    ImageVectorPreviewSettingsUi(
        previewSettings = previewSettings,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalJewelApi::class)
@Composable
private fun ImageVectorPreviewSettingsUi(
    previewSettings: PreviewSettings,
    onAction: (SettingsAction) -> Unit,
) {
    val component = LocalComponent.current

    VerticallyScrollableContainer {
        Column {
            Group(text = stringResource("settings.imagevector.preview.ide.preview.header")) {
                CheckboxSettingsRow(
                    text = stringResource("settings.imagevector.preview.ide.option.previewer"),
                    infoText = stringResource("settings.imagevector.preview.ide.option.previewer.description"),
                    checked = previewSettings.showImageVectorPreview,
                    onCheckedChange = {
                        onAction(UpdateImageVectorPreview(it))
                        promptRestart(component)
                    },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.imagevector.preview.ide.option.projectview"),
                    infoText = stringResource("settings.imagevector.preview.ide.option.projectview.description"),
                    checked = previewSettings.showIconsInProjectView,
                    onCheckedChange = {
                        onAction(UpdateIconsInProjectView(it))
                        promptRestart(component)
                    },
                )
                CheckboxSettingsRow(
                    text = stringResource("settings.imagevector.preview.ide.option.guttericon"),
                    infoText = stringResource("settings.imagevector.preview.ide.option.guttericon.description"),
                    checked = previewSettings.showImageVectorGutterIcon,
                    onCheckedChange = {
                        onAction(UpdateImageVectorGutterIcon(it))
                        promptRestart(component)
                    },
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

private fun promptRestart(component: Component) {
    val shouldRestart = MessageDialogBuilder
        .yesNo(
            title = message("settings.restart.dialog.title"),
            message = message("settings.restart.dialog.message"),
        )
        .yesText(message("settings.restart.dialog.now"))
        .noText(message("settings.restart.dialog.later"))
        .ask(component)

    if (shouldRestart) {
        ApplicationManagerEx.getApplicationEx().restart(true)
    }
}

@Preview
@Composable
private fun ImageVectorPreviewSettingsPreview() = PreviewTheme {
    var showImageVectorPreview by rememberMutableState { true }
    var showIconsInProjectView by rememberMutableState { true }
    var showImageVectorGutterIcon by rememberMutableState { true }
    var previewType by rememberMutableState { PreviewType.Auto }

    ImageVectorPreviewSettingsUi(
        previewSettings = PreviewSettings(
            showImageVectorPreview = showImageVectorPreview,
            showIconsInProjectView = showIconsInProjectView,
            showImageVectorGutterIcon = showImageVectorGutterIcon,
            previewType = previewType,
        ),
        onAction = {
            when (it) {
                is UpdateImageVectorPreview -> showImageVectorPreview = it.enabled
                is UpdateIconsInProjectView -> showIconsInProjectView = it.enabled
                is UpdateImageVectorGutterIcon -> showImageVectorGutterIcon = it.enabled
                is UpdatePreviewType -> previewType = it.previewType
                else -> {}
            }
        },
    )
}
