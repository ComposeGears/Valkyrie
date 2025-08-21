package io.github.composegears.valkyrie

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.rememberNavController
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.colored.ValkyrieLogo
import io.github.composegears.valkyrie.compose.icons.outlined.Dark
import io.github.composegears.valkyrie.compose.icons.outlined.Light
import io.github.composegears.valkyrie.compose.icons.outlined.Settings
import io.github.composegears.valkyrie.flow.simple.SimpleConversionFlow
import io.github.composegears.valkyrie.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.ProvideSharedTransition
import io.github.composegears.valkyrie.ui.theme.LocalTheme
import io.github.composegears.valkyrie.ui.theme.ValkyrieTheme

@Composable
fun ValkyrieApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController(startDestination = IntroScreen)

    ValkyrieTheme {
        ProvideSharedTransition {
            Row(modifier = modifier) {
                NavigationRail(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    VerticalSpacer(4.dp)
                    LogoItem(
                        onClick = {
                            // TODO: open about
                        },
                    )
                    WeightSpacer()
                    SettingsItem(
                        onClick = {
                            // TODO: open settings screen
                        },
                    )
                    ThemeToggle()
                    VerticalSpacer(4.dp)
                }
                Navigation(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    destinations = arrayOf(IntroScreen, SimpleConversionFlow),
                )
            }
        }
    }
}

@Composable
private fun LogoItem(onClick: () -> Unit) {
    Image(
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(8.dp),
        imageVector = ValkyrieIcons.Colored.ValkyrieLogo,
        contentDescription = null,
    )
}

@Composable
private fun SettingsItem(onClick: () -> Unit) {
    NavigationRailItem(
        selected = false,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = ValkyrieIcons.Outlined.Settings,
                contentDescription = null,
            )
        },
    )
}

@Composable
private fun ThemeToggle() {
    val localTheme = LocalTheme.current

    NavigationRailItem(
        selected = false,
        onClick = {
            localTheme.toggle()
        },
        icon = {
            AnimatedContent(
                targetState = when {
                    localTheme.isDarkTheme -> ValkyrieIcons.Outlined.Light
                    else -> ValkyrieIcons.Outlined.Dark
                },
            ) { icon ->
                Icon(imageVector = icon, contentDescription = null)
            }
        },
    )
}
