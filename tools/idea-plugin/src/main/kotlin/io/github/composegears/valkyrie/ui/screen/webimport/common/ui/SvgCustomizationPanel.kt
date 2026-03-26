package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.intellij.ui.ColorChooserService
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.settings.DropdownSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgCustomizationCapabilities
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgImportSettings
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgImportSettings.Companion.MAX_SIZE
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgImportSettings.Companion.MIN_SIZE
import io.github.composegears.valkyrie.util.stringResource
import java.awt.Color as AwtColor
import kotlin.math.roundToInt
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.ExperimentalJewelApi
import org.jetbrains.jewel.foundation.LocalComponent
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.HorizontallyScrollableContainer
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.Slider
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle
import org.jetbrains.jewel.ui.util.fromArgbHexStringOrNull
import org.jetbrains.jewel.ui.util.toArgbHexString

private val rotationOptions = listOf(0, 90, 180, 270)
private const val RECENT_COLOR_SLOTS = 4
private val presetColors = listOf("#FFFFFF")

private val defaultPickerColor = Color(0xFF68A9E0)

private fun Color.toAwtColor(): AwtColor = AwtColor(
    (red * 255).roundToInt(),
    (green * 255).roundToInt(),
    (blue * 255).roundToInt(),
)

private fun AwtColor.toRgbHexString(): String = toArgbHexString()
    .removePrefix("#")
    .takeLast(6)
    .uppercase()
    .let { "#$it" }

private fun String.toComposeColorOrNull(): Color? = Color.fromArgbHexStringOrNull(this)

@OptIn(ExperimentalJewelApi::class)
@Composable
fun SvgCustomizationPanel(
    settings: SvgImportSettings,
    recentColors: List<String>,
    lastCustomColor: String?,
    capabilities: SvgCustomizationCapabilities,
    onSettingsChange: (SvgImportSettings) -> Unit,
    onCustomColorPicked: (String) -> Unit,
    onResetCustomization: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val noneLabel = stringResource("web.import.font.customize.none")
    val customLabel = stringResource("web.import.font.customize.color.custom")
    val originalLabel = stringResource("web.import.font.customize.color.original")
    val component = LocalComponent.current
    val customColor = lastCustomColor?.toComposeColorOrNull()

    Column(modifier = modifier) {
        CustomizationToolbar(
            onClose = onClose,
            onReset = onResetCustomization,
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
                    HorizontallyScrollableContainer {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ColorSwatch(
                                label = noneLabel,
                                color = null,
                                selected = settings.color == null,
                                isNoneSwatch = true,
                                onClick = { onSettingsChange(settings.copy(color = null)) },
                            )
                            presetColors.forEach { hex ->
                                ColorSwatch(
                                    label = hex,
                                    color = Color.fromArgbHexStringOrNull(hex) ?: defaultPickerColor,
                                    selected = settings.color == hex,
                                    onClick = { onSettingsChange(settings.copy(color = hex)) },
                                )
                            }
                            repeat(RECENT_COLOR_SLOTS) { index ->
                                val recentColorHex = recentColors.getOrNull(index)
                                ColorSwatch(
                                    label = "",
                                    color = recentColorHex?.toComposeColorOrNull(),
                                    selected = recentColorHex != null && settings.color == recentColorHex,
                                    isPlaceholder = recentColorHex == null,
                                    onClick = {
                                        recentColorHex?.let { onSettingsChange(settings.copy(color = it)) }
                                    },
                                )
                            }
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            val chosenColor = ColorChooserService.getInstance().showDialog(
                                parent = component,
                                caption = customLabel,
                                preselectedColor = (settings.color?.let(Color::fromArgbHexStringOrNull) ?: customColor
                                ?: defaultPickerColor)
                                    .toAwtColor(),
                                enableOpacity = false,
                            )
                            chosenColor?.let {
                                onCustomColorPicked(it.toRgbHexString())
                            }
                        },
                    ) {
                        Text(text = customLabel)
                    }
                }

                var size by rememberMutableState(settings.size) { settings.size.toFloat() }
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
                    valueRange = MIN_SIZE.toFloat()..MAX_SIZE.toFloat(),
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
    isPlaceholder: Boolean = false,
    isNoneSwatch: Boolean = false,
    onClick: () -> Unit,
) {
    val colors = JewelTheme.globalColors
    val borderColor = with(colors) {
        when {
            selected -> text.normal
            isPlaceholder -> borders.normal.copy(alpha = 0.12f)
            else -> borders.normal
        }
    }
    val backgroundColor = when {
        color != null -> color
        isPlaceholder -> colors.panelBackground.copy(alpha = 0.38f)
        else -> colors.panelBackground
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(width = 2.dp, color = borderColor, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (isNoneSwatch) {
            val glyphColor = if (selected) colors.text.normal else colors.text.normal.copy(alpha = 0.8f)
            NoneGlyph(color = glyphColor)
        } else if (color == null && !isPlaceholder) {
            Text(text = label.take(1))
        }
    }
}

@Composable
private fun NoneGlyph(color: Color) {
    Canvas(modifier = Modifier.size(15.dp)) {
        val strokeWidth = 1.6.dp.toPx()
        drawCircle(
            color = color,
            style = Stroke(width = strokeWidth),
        )
        drawLine(
            color = color,
            start = Offset(x = size.width * 0.18f, y = size.height * 0.18f),
            end = Offset(x = size.width * 0.82f, y = size.height * 0.82f),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round,
        )
    }
}

@Preview
@Composable
private fun SvgCustomizationPanelPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    var settings by rememberMutableState { SvgImportSettings(color = "#4F8FBA", rotation = 90) }

    SvgCustomizationPanel(
        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(JewelTheme.globalColors.borders.normal),
        settings = settings,
        recentColors = listOf("#4F8FBA", "#1F8A70"),
        lastCustomColor = "#4F8FBA",
        capabilities = SvgCustomizationCapabilities(),
        onClose = {},
        onSettingsChange = { settings = it },
        onCustomColorPicked = { settings = settings.copy(color = it) },
        onResetCustomization = { settings = SvgImportSettings() },
    )
}
