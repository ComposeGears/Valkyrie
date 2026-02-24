package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.standard.model.SizeSettings
import io.github.composegears.valkyrie.util.stringResource
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Slider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle

/**
 * Generic customization panel for icon size settings
 *
 * @param settings Current size settings
 * @param onSettingsChange Callback when settings change
 * @param onClose Callback when close is clicked
 * @param sizeLabel The label text displayed next to the current size value
 * @param modifier Modifier for the panel
 */
@Composable
fun IconSizeCustomization(
    settings: SizeSettings,
    onSettingsChange: (SizeSettings) -> Unit,
    onClose: () -> Unit,
    sizeLabel: String,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableFloatStateOf(settings.size.toFloat()) }

    Column(modifier = modifier) {
        CustomizationToolbar(
            onClose = onClose,
            onReset = {
                size = SizeSettings.DEFAULT_SIZE.toFloat()
                onSettingsChange(SizeSettings())
            },
            isModified = settings.isModified,
        )
        HorizontalDivider(color = LocalGroupHeaderStyle.current.colors.divider)
        VerticallyScrollableContainer {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                CenterVerticalRow(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = sizeLabel)
                    WeightSpacer()
                    InfoText(text = stringResource("web.import.font.customize.px.suffix", size.roundToInt()))
                }
                Slider(
                    value = size,
                    onValueChange = {
                        size = it
                        onSettingsChange(settings.copy(size = size.roundToInt()))
                    },
                    valueRange = SizeSettings.MIN_SIZE.toFloat()..SizeSettings.MAX_SIZE.toFloat(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconSizeCustomizationPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    var settings by rememberMutableState { SizeSettings() }

    IconSizeCustomization(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(JewelTheme.globalColors.borders.normal),
        settings = settings,
        sizeLabel = stringResource("web.import.font.customize.size"),
        onClose = {},
        onSettingsChange = { settings = it },
    )
}
