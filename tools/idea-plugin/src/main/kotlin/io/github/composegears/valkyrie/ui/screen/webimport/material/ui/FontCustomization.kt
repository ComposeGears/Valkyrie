package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.LinkIcon
import io.github.composegears.valkyrie.jewel.settings.DropdownSettingsRow
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.CustomizationToolbar
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.FontSettings
import io.github.composegears.valkyrie.util.ValkyrieBundle.message
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.CheckboxRow
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle
import org.jetbrains.jewel.ui.icons.AllIconsKeys

private const val BASE_URL = "https://m3.material.io/styles/icons/applying-icons"
private const val FILL_HELP_URL = "$BASE_URL#ebb3ae7d-d274-4a25-9356-436e82084f1f"
private const val WEIGHT_HELP_URL = "$BASE_URL#d7f45762-67ac-473d-95b0-9214c791e242"
private const val GRADE_HELP_URL = "$BASE_URL#3ad55207-1cb0-43af-8092-fad2762f69f7"
private const val OPTICAL_SIZE_HELP_URL = "$BASE_URL#b41cbc01-9b49-4a44-a525-d153d1ea1425"

@Composable
fun FontCustomization(
    fontSettings: FontSettings,
    onSettingsChange: (FontSettings) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        CustomizationToolbar(
            onClose = onClose,
            onReset = { onSettingsChange(FontSettings()) },
            isModified = fontSettings.isModified,
        )
        HorizontalDivider(color = LocalGroupHeaderStyle.current.colors.divider)
        FontPlayground(
            fontSettings = fontSettings,
            onSettingsChange = onSettingsChange,
        )
    }
}

@Composable
private fun FontPlayground(
    fontSettings: FontSettings,
    onSettingsChange: (FontSettings) -> Unit,
) {
    val dropdownWidth = 80.dp

    VerticallyScrollableContainer {
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Spacer(16.dp)
            CenterVerticalRow {
                CheckboxRow(
                    checked = fontSettings.fill,
                    onCheckedChange = { onSettingsChange(fontSettings.copy(fill = it)) },
                ) {
                    Text(text = stringResource("web.import.font.customize.material.fill"))
                }
                Spacer(8.dp)
                LinkIcon(
                    key = AllIconsKeys.General.ContextHelp,
                    contentDescription = stringResource("accessibility.help"),
                    url = FILL_HELP_URL,
                )
            }
            Spacer(16.dp)
            CenterVerticalRow {
                DropdownSettingsRow(
                    text = stringResource("web.import.font.customize.material.weight"),
                    items = listOf(100, 200, 300, 400, 500, 600, 700),
                    current = fontSettings.weight,
                    onSelectItem = {
                        onSettingsChange(fontSettings.copy(weight = it))
                    },
                    comboxModifier = Modifier.width(dropdownWidth),
                    dropdownHorizontalPadding = 28.dp,
                )
                Spacer(8.dp)
                LinkIcon(
                    key = AllIconsKeys.General.ContextHelp,
                    contentDescription = stringResource("accessibility.help"),
                    url = WEIGHT_HELP_URL,
                )
            }
            Spacer(16.dp)
            CenterVerticalRow {
                DropdownSettingsRow(
                    text = stringResource("web.import.font.customize.material.grade"),
                    items = listOf(-25, 0, 200),
                    current = fontSettings.grade,
                    onSelectItem = {
                        onSettingsChange(fontSettings.copy(grade = it))
                    },
                    comboxModifier = Modifier.width(dropdownWidth),
                    dropdownHorizontalPadding = 33.dp,
                )
                Spacer(8.dp)
                LinkIcon(
                    key = AllIconsKeys.General.ContextHelp,
                    contentDescription = stringResource("accessibility.help"),
                    url = GRADE_HELP_URL,
                )
            }
            Spacer(16.dp)
            CenterVerticalRow {
                DropdownSettingsRow(
                    text = stringResource("web.import.font.customize.material.optical.size"),
                    items = listOf(20f, 24f, 40f, 48f),
                    current = fontSettings.opticalSize,
                    onSelectItem = {
                        onSettingsChange(fontSettings.copy(opticalSize = it))
                    },
                    transform = {
                        message("web.import.font.customize.material.dp.suffix", it.toInt())
                    },
                    comboxModifier = Modifier.width(dropdownWidth),
                )
                Spacer(8.dp)
                LinkIcon(
                    key = AllIconsKeys.General.ContextHelp,
                    contentDescription = stringResource("accessibility.help"),
                    url = OPTICAL_SIZE_HELP_URL,
                )
            }
        }
    }
}

@Preview
@Composable
private fun FontCustomizationPreview() = PreviewTheme(alignment = Alignment.TopEnd) {
    var settings by rememberMutableState { FontSettings(fill = true) }

    FontCustomization(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            .background(JewelTheme.globalColors.borders.normal),
        fontSettings = settings,
        onClose = {},
        onSettingsChange = { settings = it },
    )
}
