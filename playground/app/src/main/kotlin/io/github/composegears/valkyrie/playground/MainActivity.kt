package io.github.composegears.valkyrie.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.playground.icons.backing.BackingIcons
import io.github.composegears.valkyrie.playground.icons.backing.colored.Videocam
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Add
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Appearance
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Arrow
import io.github.composegears.valkyrie.playground.icons.backing.outlined.ArrowLeft
import io.github.composegears.valkyrie.playground.icons.backing.outlined.ArrowRight
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Brightness
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Car
import io.github.composegears.valkyrie.playground.icons.backing.outlined.Changelog
import io.github.composegears.valkyrie.playground.icons.lazy.LazyIcons
import io.github.composegears.valkyrie.playground.icons.lazy.colored.Videocam
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Add
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Appearance
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Arrow
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.ArrowLeft
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.ArrowRight
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Brightness
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Car
import io.github.composegears.valkyrie.playground.icons.lazy.outlined.Changelog
import io.github.composegears.valkyrie.playground.ui.theme.PlaygroundTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PlaygroundTheme {
                val iconsSet = remember {
                    listOf(
                        IconsSet(
                            name = "Backing property",
                            icons = listOf(
                                BackingIcons.Colored.Videocam,
                                BackingIcons.Outlined.Add,
                                BackingIcons.Outlined.Appearance,
                                BackingIcons.Outlined.Arrow,
                                BackingIcons.Outlined.ArrowLeft,
                                BackingIcons.Outlined.ArrowRight,
                                BackingIcons.Outlined.Brightness,
                                BackingIcons.Outlined.Car,
                                BackingIcons.Outlined.Changelog,
                            ),
                        ),
                        IconsSet(
                            name = "Lazy property",
                            icons = listOf(
                                LazyIcons.Colored.Videocam,
                                LazyIcons.Outlined.Add,
                                LazyIcons.Outlined.Appearance,
                                LazyIcons.Outlined.Arrow,
                                LazyIcons.Outlined.ArrowLeft,
                                LazyIcons.Outlined.ArrowRight,
                                LazyIcons.Outlined.Brightness,
                                LazyIcons.Outlined.Car,
                                LazyIcons.Outlined.Changelog,
                            ),
                        ),
                    )
                }
                Surface(modifier = Modifier.systemBarsPadding()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        iconsSet.forEach { (name, icons) ->
                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary)
                                        .padding(vertical = 4.dp),
                                ) {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        text = name,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                            }
                            items(icons) { icon ->
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp,
                                    ),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Image(
                                        imageVector = icon,
                                        contentDescription = null,
                                    )
                                    Text(
                                        text = icon.name,
                                        style = MaterialTheme.typography.bodySmall,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

data class IconsSet(
    val name: String,
    val icons: List<ImageVector>,
)
