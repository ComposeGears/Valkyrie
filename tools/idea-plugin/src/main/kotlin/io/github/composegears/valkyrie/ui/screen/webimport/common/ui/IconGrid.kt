package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.jewel.ui.component.VerticalScrollbar

@Composable
fun IconGrid(
    state: LazyGridState,
    modifier: Modifier = Modifier,
    content: LazyGridScope.() -> Unit,
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            state = state,
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(100.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
        VerticalScrollbar(
            scrollState = state,
            modifier = Modifier.fillMaxHeight()
                .align(Alignment.CenterEnd)
                .padding(end = 4.dp, top = 8.dp, bottom = 4.dp),
        )
    }
}
