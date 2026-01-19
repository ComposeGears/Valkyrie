package io.github.composegears.valkyrie.jewel.banner

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.intellij.codeInsight.template.emmet.generators.LoremGenerator
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.ErrorBanner
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.InfoBanner
import io.github.composegears.valkyrie.jewel.banner.BannerMessage.WarningBanner
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import kotlin.random.Random
import kotlinx.coroutines.delay
import org.jetbrains.jewel.ui.component.DefaultButton
import org.jetbrains.jewel.ui.component.InlineErrorBanner
import org.jetbrains.jewel.ui.component.InlineSuccessBanner
import org.jetbrains.jewel.ui.component.InlineWarningBanner
import org.jetbrains.jewel.ui.component.Text

@Composable
fun BannerHost(
    state: BannerState,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(8.dp),
) {
    val currentBannerData = state.currentBannerData
    LaunchedEffect(currentBannerData) {
        if (currentBannerData != null) {
            val duration = currentBannerData.message.duration.toMillis()
            delay(duration)
            currentBannerData.dismiss()
        }
    }

    AnimatedContent(
        modifier = modifier
            .fillMaxWidth()
            .padding(paddingValues),
        targetState = currentBannerData,
        transitionSpec = {
            if (initialState != targetState) {
                (slideInVertically { height -> height } + fadeIn()) togetherWith
                        slideOutVertically { height -> height } + fadeOut()
            } else {
                slideInVertically { height -> -height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
            }.using(
                SizeTransform(clip = false)
            )
        },
        label = "animation"
    ) { data ->
        when (data) {
            null -> Spacer(modifier = Modifier.fillMaxWidth())
            else -> BannerRender(data)
        }
    }
}

@Composable
private fun BannerRender(bannerData: BannerData) {
    when (val message = bannerData.message) {
        is InfoBanner -> InlineSuccessBanner(text = message.text)
        is WarningBanner -> InlineWarningBanner(text = message.text)
        is ErrorBanner -> InlineErrorBanner(text = message.text)
    }
}

internal fun BannerDuration.toMillis(): Long {
    return when (this) {
        BannerDuration.Indefinite -> Long.MAX_VALUE
        BannerDuration.Long -> 10_000L
        BannerDuration.Short -> 4_000L
    }
}

private fun lorem() = LoremGenerator().generate(Random.nextInt(3, 25), true)

@Preview
@Composable
private fun BannerPreview() = PreviewTheme {
    val bannerManager = rememberBannerManager()

    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DefaultButton(
            onClick = {
                bannerManager.show(message = InfoBanner(text = lorem()))
            },
            content = {
                Text(text = "Success banner")
            }
        )
        DefaultButton(
            onClick = {
                bannerManager.show(message = WarningBanner(text = lorem()))
            },
            content = {
                Text(text = "Warning banner")
            }
        )
        DefaultButton(
            onClick = {
                bannerManager.show(message = ErrorBanner(text = lorem()))
            },
            content = {
                Text(text = "Error banner")
            }
        )
    }
    BannerHost(
        modifier = Modifier.align(Alignment.BottomCenter),
        state = LocalGlobalBannerState.current
    )
}