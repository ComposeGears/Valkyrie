@file:Suppress("DEPRECATION")

package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.intellij.ui.ColorChooser
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.settings.DropdownSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SizeSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgCustomizationCapabilities
import io.github.composegears.valkyrie.util.stringResource
import java.awt.Color as AwtColor
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.LocalComponent
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Slider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle

private val rotationOptions = listOf(0, 90, 180, 270)
private val presetColors = listOf(
    "#F4CC3A",
    "#63D3AF",
    "#68A9E0",
    "#9D86E3",
    "#FF9A62",
)

private fun String.toComposeColor(): Color {
    val awtColor = AwtColor.decode(this)
    return Color(awtColor.red, awtColor.green, awtColor.blue)
}

private fun String.toAwtColorOrNull(): AwtColor? = runCatching { AwtColor.decode(this) }.getOrNull()

private fun AwtColor.toHexColor(): String = String.format("#%02X%02X%02X", red, green, blue)

@Composable
fun SvgCustomizationPanel(
    settings: SizeSettings,
    capabilities: SvgCustomizationCapabilities,
    onSettingsChange: (SizeSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val noneLabel = stringResource("web.import.font.customize.none")
    val customLabel = stringResource("web.import.font.customize.color.custom")
    val originalLabel = stringResource("web.import.font.customize.color.original")
    val component = LocalComponent.current

    Column(modifier = modifier) {
        CustomizationToolbar(
            onClose = onClose,
            onReset = { onSettingsChange(SizeSettings()) },
            isModified = settings.isModified,
        )
        HorizontalDivider(color = LocalGroupHeaderStyle.current.colors.divider)
        VerticallyScrollableContainer {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (capabilities.supportsColor) {
                    CenterVerticalRow(modifier = Modifier.padding(horizontal = 4.dp)) {
                        Text(text = stringResource("web.import.font.customize.color"))
                        WeightSpacer()
                        InfoText(text = settings.color ?: originalLabel)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ColorSwatch(
                            label = noneLabel,
                            color = null,
                            selected = settings.color == null,
                            onClick = { onSettingsChange(settings.copy(color = null)) },
                        )
                        presetColors.forEach { hex ->
                            ColorSwatch(
                                label = hex,
                                color = hex.toComposeColor(),
                                selected = settings.color == hex,
                                onClick = { onSettingsChange(settings.copy(color = hex)) },
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            val chosenColor = ColorChooser.chooseColor(
                                component,
                                customLabel,
                                settings.color?.toAwtColorOrNull() ?: AwtColor.decode("#68A9E0"),
                                false,
                            )
                            if (chosenColor != null) {
                                onSettingsChange(settings.copy(color = chosenColor.toHexColor()))
                            }
                        },
                    ) {
                        Text(text = customLabel)
                    }
                }

                var size by rememberMutableState { settings.size.toFloat() }
                LaunchedEffect(settings.size) {
                    size = settings.size.toFloat()
                }
                CenterVerticalRow(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(text = stringResource("web.import.font.customize.size"))
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

                if (capabilities.supportsRotation) {
                    DropdownSettingsRow(
                        text = stringResource("web.import.font.customize.rotate"),
                        current = settings.rotation,
                        items = rotationOptions,
                        onSelectItem = { onSettingsChange(settings.copy(rotation = it)) },
                        comboxModifier = Modifier.width(90.dp),
                        transform = {
                            when (it) {
                                0 -> noneLabel
                                else -> "$it${'\u00B0'}"
                            }
                        },
                    )
                }

                if (capabilities.supportsFlip) {
                    Text(text = stringResource("web.import.font.customize.flip"))
                    CheckboxRow(
                        checked = settings.flipHorizontally,
                        onCheckedChange = {
                            onSettingsChange(settings.copy(flipHorizontally = it))
                        },
                    ) {
                        Text(text = stringResource("web.import.font.customize.flip.horizontal"))
                    }
                    CheckboxRow(
                        checked = settings.flipVertically,
                        onCheckedChange = {
                            onSettingsChange(settings.copy(flipVertically = it))
                        },
                    ) {
                        Text(text = stringResource("web.import.font.customize.flip.vertical"))
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSwatch(
    label: String,
    color: Color?,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = when {
        selected -> JewelTheme.globalColors.text.normal
        else -> JewelTheme.globalColors.borders.normal
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color ?: JewelTheme.globalColors.panelBackground)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (color == null) {
            Text(text = label.take(1))
        }
    }
}

@Preview
@Composable
private fun SvgCustomizationPanelPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    var settings by rememberMutableState { SizeSettings(color = "#4F8FBA", rotation = 90) }

    SvgCustomizationPanel(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(JewelTheme.globalColors.borders.normal),
        settings = settings,
        capabilities = SvgCustomizationCapabilities(),
        onClose = {},
        onSettingsChange = { settings = it },
    )
}
