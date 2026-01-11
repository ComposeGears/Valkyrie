package io.github.composegears.valkyrie.ui.screen.editor.edit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.common.picker.UniversalPicker
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource

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
            TopAppBar {
                BackAction(onBack = onBack)
                AppBarTitle(title = stringResource("editor.edit.header"))
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
