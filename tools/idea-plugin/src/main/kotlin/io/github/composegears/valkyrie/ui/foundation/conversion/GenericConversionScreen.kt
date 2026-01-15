package io.github.composegears.valkyrie.ui.foundation.conversion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.CopyAction
import io.github.composegears.valkyrie.jewel.EditToggleAction
import io.github.composegears.valkyrie.jewel.PreviewToggleAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.ExpandedAnimatedContent
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.ui.foundation.HorizontalDivider

/**
 * Represents the expanded action panels in conversion screens.
 */
enum class ConversionExpandedAction {
    None,
    Edit,
    Preview,
}

/**
 * Generic conversion screen layout that can be reused for different conversion modes.
 * Provides a consistent structure with:
 * - Top app bar with common actions (Back, Edit, Preview, Copy, Settings)
 * - Expandable panels for editing and preview
 * - Code viewer area
 *
 * @param title The title to display in the app bar
 * @param iconName The current icon name (for editing)
 * @param codeContent The code content to display and edit
 * @param irImageVector The IR representation for preview
 * @param onBack Callback when back button is clicked
 * @param onIconNameChange Callback when icon name is changed
 * @param onCopyCode Callback to copy code to clipboard
 * @param onOpenSettings Callback to open settings
 * @param editPanel Composable for the edit action panel
 * @param previewPanel Composable for the preview action panel
 * @param codeViewer Composable for displaying and editing code
 */
@Composable
fun GenericConversionScreen(
    title: String,
    iconName: String,
    codeContent: String,
    irImageVector: IrImageVector,
    onBack: () -> Unit,
    onIconNameChange: (String) -> Unit,
    onCopyCode: (String) -> Unit,
    onOpenSettings: () -> Unit,
    editPanel: @Composable (iconName: String, onNameChange: (String) -> Unit) -> Unit,
    previewPanel: @Composable (irImageVector: IrImageVector) -> Unit,
    codeViewer: @Composable (text: String, onChange: (String) -> Unit) -> Unit,
    modifier: Modifier = Modifier,
) {
    var codePreview by rememberMutableState(codeContent) { codeContent }
    var expandedAction by rememberMutableState { ConversionExpandedAction.None }

    Column(modifier = modifier.fillMaxSize()) {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = title)
            WeightSpacer()
            EditToggleAction(
                onEdit = { selected ->
                    expandedAction = if (selected) ConversionExpandedAction.Edit else ConversionExpandedAction.None
                },
                selected = expandedAction == ConversionExpandedAction.Edit,
            )
            PreviewToggleAction(
                onPreview = { selected ->
                    expandedAction = if (selected) ConversionExpandedAction.Preview else ConversionExpandedAction.None
                },
                selected = expandedAction == ConversionExpandedAction.Preview,
            )
            CopyAction(onCopy = { onCopyCode(codePreview) })
            SettingsAction(openSettings = onOpenSettings)
        }
        ExpandedAnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = expandedAction,
        ) { action ->
            when (action) {
                ConversionExpandedAction.Edit -> {
                    editPanel(iconName, onIconNameChange)
                }
                ConversionExpandedAction.Preview -> {
                    previewPanel(irImageVector)
                }
                ConversionExpandedAction.None -> {
                    Spacer(modifier = Modifier.fillMaxWidth())
                }
            }
        }
        HorizontalDivider()
        codeViewer(codePreview) { codePreview = it }
    }
}
