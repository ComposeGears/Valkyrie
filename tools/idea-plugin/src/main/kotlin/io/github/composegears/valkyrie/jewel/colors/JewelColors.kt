package io.github.composegears.valkyrie.jewel.colors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.styling.LocalGroupHeaderStyle

val JewelTheme.Companion.softContentColor: Color
    @Composable
    @ReadOnlyComposable
    get() = JewelTheme.contentColor.copy(.5f)

val JewelTheme.Companion.primaryColor: Color
    @Composable
    @ReadOnlyComposable
    get() = JewelTheme.globalColors.outlines.focused

val JewelTheme.Companion.errorFocused: Color
    @Composable
    @ReadOnlyComposable
    get() = JewelTheme.globalColors.outlines.focusedError

/** Background color for floating/overlay surfaces (pills, side panels). */
val JewelTheme.Companion.overlay: Color
    @Composable
    @ReadOnlyComposable
    get() = JewelTheme.globalColors.borders.normal

/** Divider color for content rendered on top of an [overlay] surface. */
val JewelTheme.Companion.onOverlay: Color
    @Composable
    @ReadOnlyComposable
    get() = if (JewelTheme.isDark) {
        LocalGroupHeaderStyle.current.colors.divider
    } else {
        JewelTheme.globalColors.borders.disabled
    }

@Preview
@Composable
private fun JewelColorsPreview() = PreviewTheme {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(text = "— Custom —")
        ColorCell(JewelTheme.softContentColor, "softContentColor")
        ColorCell(JewelTheme.overlay, "overlay")
        ColorCell(JewelTheme.onOverlay, "onOverlay")
        Spacer(modifier = Modifier.size(8.dp))
        Text(text = "— Global —")
        ColorCell(JewelTheme.contentColor, "contentColor")
        ColorCell(JewelTheme.globalColors.panelBackground, "global.panelBackground")
        ColorCell(JewelTheme.globalColors.borders.normal, "global.borders.normal")
        ColorCell(JewelTheme.globalColors.borders.focused, "global.borders.focused")
        ColorCell(JewelTheme.globalColors.borders.disabled, "global.borders.disabled")
        ColorCell(JewelTheme.globalColors.outlines.focused, "global.outlines.focused")
        ColorCell(JewelTheme.globalColors.outlines.error, "global.outlines.error")
        ColorCell(JewelTheme.globalColors.outlines.focusedError, "global.outlines.focusedError")
        ColorCell(JewelTheme.globalColors.outlines.warning, "global.outlines.warning")
        ColorCell(JewelTheme.globalColors.outlines.focusedWarning, "global.outlines.focusedWarning")
        ColorCell(JewelTheme.globalColors.text.normal, "global.text.normal")
        ColorCell(JewelTheme.globalColors.text.selected, "global.text.selected")
        ColorCell(JewelTheme.globalColors.text.warning, "global.text.warning")
        ColorCell(JewelTheme.globalColors.text.error, "global.text.error")
        ColorCell(JewelTheme.globalColors.text.disabled, "global.text.disabled")
        ColorCell(JewelTheme.globalColors.text.disabledSelected, "global.text.disabledSelected")
    }
}

@Composable
private fun ColorCell(color: Color, name: String) {
    CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Spacer(
            modifier = Modifier
                .size(24.dp)
                .background(color),
        )
        Text(text = name)
    }
}
