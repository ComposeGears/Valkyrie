package io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.ui.picker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                Title(text = stringResource("iconpack.conversion.picker.title"))
                WeightSpacer()
                SettingsAction(openSettings = openSettings)
            }
        },
    )
}

@Preview
@Composable
private fun IconPackPickerPreview() = PreviewTheme(alignment = Alignment.Center) {
    IconPackPickerStateUi(
        onPickerEvent = {},
        onBack = {},
        openSettings = {},
    )
}
