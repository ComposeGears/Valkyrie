package io.github.composegears.valkyrie.jewel.button

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import org.jetbrains.jewel.ui.component.IconActionButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.icon.IconKey
import org.jetbrains.jewel.ui.icons.AllIconsKeys

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipIconButton(
    key: IconKey,
    contentDescription: String?,
    onClick: () -> Unit,
    tooltipText: String,
    modifier: Modifier = Modifier,
) {
    IconActionButton(
        modifier = modifier,
        key = key,
        onClick = onClick,
        focusable = false,
        contentDescription = contentDescription,
        tooltip = {
            Text(text = tooltipText)
        },
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    tooltipText: String,
    modifier: Modifier = Modifier,
) {
    IconActionButton(
        modifier = modifier,
        painter = rememberVectorPainter(imageVector),
        onClick = onClick,
        focusable = false,
        contentDescription = contentDescription,
        tooltip = {
            Text(text = tooltipText)
        },
    )
}

@Preview
@Composable
private fun TooltipIconButtonPreview() = PreviewTheme {
    TooltipIconButton(
        key = AllIconsKeys.Actions.AddFile,
        contentDescription = null,
        onClick = {},
        tooltipText = "Tooltip text",
    )
}
