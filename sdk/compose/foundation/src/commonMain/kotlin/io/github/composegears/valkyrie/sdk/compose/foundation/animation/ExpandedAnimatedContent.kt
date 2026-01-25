package io.github.composegears.valkyrie.sdk.compose.foundation.animation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <S> ExpandedAnimatedContent(
    targetState: S,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedContentScope.(targetState: S) -> Unit,
) {
    AnimatedContent(
        modifier = modifier,
        targetState = targetState,
        transitionSpec = {
            if (initialState != targetState) {
                (slideInVertically { height -> -height } + fadeIn()) togetherWith
                    slideOutVertically { height -> -height } + fadeOut()
            } else {
                slideInVertically { height -> height } + fadeIn() togetherWith
                    slideOutVertically { height -> height } + fadeOut()
            }.using(
                SizeTransform(clip = true),
            )
        },
        content = content,
    )
}
