package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.colors.primaryColor
import io.github.composegears.valkyrie.jewel.core.FocusContainer
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.animatedBorder
import io.github.composegears.valkyrie.sdk.compose.foundation.applyIf
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun IconCard(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconContent: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        FocusContainer(
            modifier = Modifier
                .width(100.dp)
                .applyIf(selected) {
                    animatedBorder(
                        colors = listOf(
                            Color.Transparent,
                            JewelTheme.primaryColor,
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )
                },
            onClick = onClick,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .padding(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    iconContent()
                }
                Text(
                    modifier = Modifier.basicMarquee(),
                    text = name,
                    style = JewelTheme.typography.small,
                    maxLines = 1,
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconCardPreview() = PreviewTheme(alignment = Alignment.Center) {
    var selected by rememberMutableState { false }

    IconCard(
        name = "Icon name",
        selected = selected,
        onClick = { selected = !selected },
        iconContent = {},
    )
}
