package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpacer(
    dp: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(dp))
}

@Composable
fun HorizontalSpacer(
    dp: Dp,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.width(dp))
}

@Composable
fun RowScope.WeightSpacer(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
) {
    Spacer(modifier = modifier.weight(weight))
}

@Composable
fun ColumnScope.WeightSpacer(
    modifier: Modifier = Modifier,
    weight: Float = 1f,
) {
    Spacer(modifier = modifier.weight(weight))
}
