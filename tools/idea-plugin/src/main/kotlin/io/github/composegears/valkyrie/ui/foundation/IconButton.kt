package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

@Composable
fun IconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    iconSize: Dp = Dp.Unspecified,
    onClick: () -> Unit,
) {
    IconButton(
        colors = colors,
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            imageVector = imageVector,
            contentDescription = null,
        )
    }
}

/**
 * A smaller variant of [IconButton] that removes the minimum interactive component size constraint.
 *
 * **Accessibility Warning:** This component bypasses Material Design's minimum touch target size
 * (48dp), which may make the button harder to interact with for some users. Use only in
 * constrained UI spaces where the standard [IconButton] size is not feasible.
 *
 * @see IconButton
 */
@Composable
fun IconButtonSmall(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(),
    iconSize: Dp = Dp.Unspecified,
    onClick: () -> Unit,
) {
    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
        IconButton(
            imageVector = imageVector,
            modifier = modifier,
            enabled = enabled,
            colors = colors,
            iconSize = iconSize,
            onClick = onClick,
        )
    }
}
