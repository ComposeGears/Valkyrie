package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.picker

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.ui.common.picker.PickerEvent
import io.github.composegears.valkyrie.ui.common.picker.UniversalPicker
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.SettingsAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
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
            TopAppBar {
                BackAction(onBack = onBack)
                AppBarTitle(title = "IconPack generation")
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
