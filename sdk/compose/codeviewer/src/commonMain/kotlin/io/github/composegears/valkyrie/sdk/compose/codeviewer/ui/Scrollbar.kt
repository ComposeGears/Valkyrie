package io.github.composegears.valkyrie.sdk.compose.codeviewer.ui

import androidx.compose.foundation.HorizontalScrollbar
import androidx.compose.foundation.LocalScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.v2.ScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxScope.VerticalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
) {
    val scrollbarStyle = LocalScrollbarStyle.current.copy(
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    )

    VerticalScrollbar(
        modifier = modifier
            .fillMaxHeight()
            .align(Alignment.CenterEnd)
            .padding(end = 4.dp, top = 8.dp, bottom = 4.dp),
        adapter = adapter,
        style = scrollbarStyle,
    )
}

@Composable
internal fun BoxScope.HorizontalScrollbar(
    adapter: ScrollbarAdapter,
    modifier: Modifier = Modifier,
) {
    val scrollbarStyle = LocalScrollbarStyle.current.copy(
        unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    )

    HorizontalScrollbar(
        modifier = modifier
            .fillMaxWidth()
            .align(Alignment.BottomStart)
            .padding(start = 8.dp, end = 20.dp, bottom = 4.dp),
        adapter = adapter,
        style = scrollbarStyle,
    )
}
