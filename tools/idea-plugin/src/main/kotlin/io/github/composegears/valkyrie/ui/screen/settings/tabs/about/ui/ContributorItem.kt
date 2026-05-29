package io.github.composegears.valkyrie.ui.screen.settings.tabs.about.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.Shimmer
import io.github.composegears.valkyrie.sdk.compose.foundation.animation.shimmer
import io.github.composegears.valkyrie.ui.screen.settings.tabs.about.domain.ContributorUiModel
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.ExternalLink
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.typography

@Composable
fun ContributorItem(
    contributor: ContributorUiModel,
    shimmer: Shimmer,
    modifier: Modifier = Modifier,
) {
    val bitmap: ImageBitmap? = remember(contributor.avatarBytes) {
        contributor.avatarBytes?.let { avatar ->
            runCatching {
                ImageIO.read(ByteArrayInputStream(avatar.bytes)).toComposeImageBitmap()
            }.getOrNull()
        }
    }

    Column(
        modifier = modifier.width(160.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (bitmap != null) {
            Image(
                modifier = Modifier.size(40.dp).clip(CircleShape),
                painter = BitmapPainter(bitmap),
                contentDescription = contributor.login,
            )
        } else {
            Spacer(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .shimmer(shimmer = shimmer, cornerRadius = 20.dp),
            )
        }
        ExternalLink(
            uri = contributor.profileUrl,
            text = contributor.login,
        )
        Text(
            text = contributor.description,
            style = JewelTheme.typography.small,
            textAlign = TextAlign.Center,
        )
    }
}
