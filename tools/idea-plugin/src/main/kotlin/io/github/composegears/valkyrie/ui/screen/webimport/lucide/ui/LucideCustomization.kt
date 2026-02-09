package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.foundation.background
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
import io.github.composegears.valkyrie.jewel.CloseAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideSettings
import io.github.composegears.valkyrie.util.stringResource
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.Link
import org.jetbrains.jewel.ui.component.Slider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle

@Composable
fun LucideCustomization(
    settings: LucideSettings,
    onSettingsChange: (LucideSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var size by remember { mutableFloatStateOf(settings.size.toFloat()) }

    Column(modifier = modifier) {
        Toolbar {
            CloseAction(onClose = onClose)
            Title(text = stringResource("web.import.font.customize.header"))
            WeightSpacer()
            Link(
                text = stringResource("web.import.font.customize.reset"),
                onClick = {
                    size = LucideSettings.DEFAULT_SIZE.toFloat()

                    onSettingsChange(LucideSettings())
                },
                enabled = settings.isModified,
            )
            Spacer(4.dp)
        }
        HorizontalDivider(color = LocalGroupHeaderStyle.current.colors.divider)
        VerticallyScrollableContainer {
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Spacer(16.dp)
                CenterVerticalRow(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = stringResource("web.import.font.customize.lucide.size"))
                    WeightSpacer()
                    InfoText(text = stringResource("web.import.font.customize.lucide.px.suffix", size.roundToInt()))
                }
                Spacer(8.dp)
                Slider(
                    value = size,
                    onValueChange = { size = it },
                    onValueChangeFinished = {
                        onSettingsChange(settings.copy(size = size.roundToInt()))
                    },
                    valueRange = 16f..48f,
                )
                Spacer(16.dp)
            }
        }
    }
}

@Preview
@Composable
private fun FontCustomizationPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    var settings by rememberMutableState { LucideSettings() }

    LucideCustomization(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(JewelTheme.globalColors.borders.normal),
        settings = settings,
        onClose = {},
        onSettingsChange = { settings = it },
    )
}
