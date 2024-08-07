@file:Suppress("NOTHING_TO_INLINE")

package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
inline fun VerticalSpacer(
    dp: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(dp))
}

@Composable
inline fun HorizontalSpacer(
    dp: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.width(dp))
}

@Composable
inline fun SizeSpacer(
    size: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.size(size))
}

@Composable
inline fun RowScope.WeightSpacer(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
) {
    Spacer(modifier = modifier.weight(weight))
}

@Composable
inline fun ColumnScope.WeightSpacer(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
) {
    Spacer(modifier = modifier.weight(weight))
}
