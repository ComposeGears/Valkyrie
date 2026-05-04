package io.github.composegears.valkyrie.ui.screen.webimport.common.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.shimmer

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
