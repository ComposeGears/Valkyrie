package io.github.composegears.valkyrie.ui.foundation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButtonRowScope.SegmentedButton(
  selected: Boolean,
  onClick: () -> Unit,
  shape: Shape,
  label: @Composable () -> Unit,
) {
  SegmentedButton(
    shape = shape,
    onClick = onClick,
    selected = selected,
    colors = SegmentedButtonDefaults.colors().copy(
      activeContainerColor = MaterialTheme.colorScheme.primary,
      activeContentColor = MaterialTheme.colorScheme.onPrimary,
      activeBorderColor = MaterialTheme.colorScheme.primary,
    ),
    icon = {},
    label = label,
  )
}
