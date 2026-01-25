package io.github.composegears.valkyrie.jewel.tooling

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.button.OutlineIconButton
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.CenterVerticalRow
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@Composable
fun PreviewNavigationControls(
    onBack: () -> Unit,
    onForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CenterVerticalRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlineIconButton(
            key = AllIconsKeys.Vcs.Arrow_left,
            contentDescription = null,
            onClick = onBack,
        )
        OutlineIconButton(
            key = AllIconsKeys.Vcs.Arrow_right,
            contentDescription = null,
            onClick = onForward,
        )
    }
}

@Preview
@Composable
private fun PreviewNavigationControlsPreview() = PreviewTheme(alignment = Alignment.Center) {
    PreviewNavigationControls(
        onBack = {},
        onForward = {},
    )
}
