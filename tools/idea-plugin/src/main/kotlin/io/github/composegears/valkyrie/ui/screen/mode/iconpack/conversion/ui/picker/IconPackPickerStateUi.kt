package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.SettingsAction
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.common.picker.UniversalPicker
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme

@Composable
fun IconPackPickerStateUi(
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
                Title(text = "IconPack generation")
                WeightSpacer()
                SettingsAction(openSettings = openSettings)
            }
        },
    )
}

@Preview
@Composable
private fun IconPackPickerPreview() = PreviewTheme {
    IconPackPickerStateUi(
        onPickerEvent = {},
        onBack = {},
        openSettings = {},
    )
}
