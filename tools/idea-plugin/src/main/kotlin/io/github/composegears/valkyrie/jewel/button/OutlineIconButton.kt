package io.github.composegears.valkyrie.jewel.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Icon
import org.jetbrains.jewel.ui.component.OutlinedButton
import org.jetbrains.jewel.ui.component.styling.ButtonMetrics
import org.jetbrains.jewel.ui.component.styling.ButtonStyle
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys
import org.jetbrains.jewel.ui.theme.outlinedButtonStyle

@Composable
fun OutlineIconButton(
    key: IconKey,
    onClick: () -> Unit,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val size = JewelTheme.outlinedButtonStyle.metrics.minSize.height
    val style = ButtonStyle(
        colors = JewelTheme.outlinedButtonStyle.colors,
        focusOutlineAlignment = JewelTheme.outlinedButtonStyle.focusOutlineAlignment,
        metrics = ButtonMetrics(
            cornerSize = JewelTheme.outlinedButtonStyle.metrics.cornerSize,
            padding = PaddingValues(0.dp),
            minSize = DpSize(size, size),
            borderWidth = JewelTheme.outlinedButtonStyle.metrics.borderWidth,
            focusOutlineExpand = JewelTheme.outlinedButtonStyle.metrics.focusOutlineExpand,
        ),
    )

    OutlinedButton(
        modifier = modifier,
        style = style,
        onClick = onClick,
        enabled = enabled,
    ) {
        val tint = when {
            enabled -> Color.Unspecified
            else -> style.colors.borderDisabled.safeAs<SolidColor>()?.value ?: Color.Unspecified
        }
        Icon(
            key = key,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Preview
@Composable
private fun OutlineIconButtonPreview() = PreviewTheme(alignment = Alignment.Center) {
    CenterVerticalRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlineIconButton(
            key = AllIconsKeys.General.Close,
            contentDescription = stringResource("accessibility.close"),
            onClick = {},
        )
        OutlineIconButton(
            key = AllIconsKeys.General.Close,
            contentDescription = stringResource("accessibility.close"),
            onClick = {},
            enabled = false,
        )
    }
}
