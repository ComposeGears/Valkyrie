package io.github.composegears.valkyrie.ui.screen.editor.edit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.foundation.picker.PickerEvent
import io.github.composegears.valkyrie.ui.foundation.picker.UniversalPicker
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun EditorSelectUi(
    onPickerEvent: (PickerEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    openSettings: () -> Unit,
) {
    UniversalPicker(
        modifier = modifier,
        onPickerEvent = onPickerEvent,
        headerSection = {
            Toolbar {
                BackAction(onBack = onBack)
                Title(text = stringResource("editor.edit.header"))
                WeightSpacer()
                SettingsAction(openSettings = openSettings)
            }
        },
    )
}

@Preview
@Composable
private fun EditorSelectPreview() = PreviewTheme {
    EditorSelectUi(
        onPickerEvent = {},
        onBack = {},
        openSettings = {},
    )
}
