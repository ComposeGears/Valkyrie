package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.compose.core.animatedBorder
import io.github.composegears.valkyrie.compose.core.animation.Shimmer
import io.github.composegears.valkyrie.compose.core.animation.shimmer
import io.github.composegears.valkyrie.compose.core.applyIf
import io.github.composegears.valkyrie.compose.ui.foundation.VerticalScrollbar

/**
 * Shared loading content for web import screens
 */
@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    message: String = "Loading icons...",
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CircularProgressIndicator()
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

/**
 * Shared error content for web import screens
 */
@Composable
fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
        )
    }
}

/**
 * Shared empty state content for web import screens
 */
@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    message: String = "No icons found",
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

/**
 * Shared category header for web import screens
 */
@Composable
fun CategoryHeader(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .padding(vertical = 8.dp)
            .padding(start = 4.dp),
        text = title,
        style = MaterialTheme.typography.titleMedium,
    )
}

/**
 * Shared icon grid for web import screens.
 * Provides a consistent grid layout with scrollbar.
 *
 * @param state The LazyGridState for the grid
 * @param modifier Modifier to be applied to the container
 * @param content The content of the grid
 */
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
        VerticalScrollbar(adapter = rememberScrollbarAdapter(state))
    }
}

/**
 * Shared icon card wrapper for web import screens.
 * Provides consistent card styling, selection state, and text display.
 *
 * @param name The name of the icon to display
 * @param selected Whether this icon is currently selected
 * @param onClick Callback when the card is clicked
 * @param modifier Modifier to be applied to the card
 * @param iconContent The icon content to display in the center
 */
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
        Card(
            modifier = Modifier
                .width(100.dp)
                .applyIf(selected) {
                    animatedBorder(
                        borderColors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.primary,
                        ),
                        shape = CardDefaults.shape,
                    )
                },
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
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
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                )
            }
        }
    }
}

/**
 * Shimmer placeholder for loading icon state.
 *
 * @param shimmer The shimmer animation to use
 * @param modifier Modifier to be applied to the shimmer placeholder
 */
@Composable
fun IconLoadingPlaceholder(
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .shimmer(shimmer = shimmer, cornerRadius = 12.dp),
    )
}


