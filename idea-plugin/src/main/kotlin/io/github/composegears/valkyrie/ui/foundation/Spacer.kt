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
fun VerticalSpacer(dp: Dp) = Spacer(modifier = Modifier.height(dp))

@Composable
fun HorizontalSpacer(dp: Dp) = Spacer(modifier = Modifier.width(dp))

@Composable
fun RowScope.WeightSpacer(weight: Float = 1f) = Spacer(modifier = Modifier.weight(weight))

@Composable
fun ColumnScope.WeightSpacer(weight: Float = 1f) = Spacer(modifier = Modifier.weight(weight))
