package io.github.composegears.valkyrie.ui.screen.webimport.lucide.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.composegears.valkyrie.compose.core.animation.Shimmer
import io.github.composegears.valkyrie.ui.screen.webimport.common.ui.IconLoadingPlaceholder
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.IconLoadState
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.model.LucideIcon

/**
 * Display component that renders a Lucide icon
 * Shows a shimmer placeholder while loading, then the actual rendered icon
 *
 */
@Composable
fun LucideIconDisplay(
    icon: LucideIcon,
    iconCacheKey: String,
    iconLoadState: IconLoadState?,
    onLoadIcon: (LucideIcon) -> Unit,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    val currentOnLoadIcon by rememberUpdatedState(onLoadIcon)

    LaunchedEffect(iconCacheKey) {
        currentOnLoadIcon(icon)
    }

    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center,
    ) {
        when (iconLoadState) {
            null, IconLoadState.Loading, IconLoadState.Error -> {
                IconLoadingPlaceholder(shimmer = shimmer)
            }
            is IconLoadState.Success -> Icon(
                imageVector = iconLoadState.imageVector,
                contentDescription = icon.name,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
