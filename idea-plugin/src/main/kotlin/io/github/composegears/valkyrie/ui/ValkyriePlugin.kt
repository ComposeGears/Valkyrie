package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination.IconPackDestinationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupScreen
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun ValkyriePlugin(
    modifier: Modifier = Modifier,
) {
    val inMemorySettings = koinInject<InMemorySettings>()

    val navController = rememberNavController(
        destinations = arrayOf(
            IntroScreen,
            SimpleModeSetupScreen,
            SimpleConversionScreen,

            IconPackDestinationScreen,
            IconPackCreationScreen,
            IconPackConversionScreen,

            CodePreviewScreen,

            SettingsScreen,
        ),
        startDestination = SettingsScreen,
    )

    Navigation(
        modifier = modifier.fillMaxSize(),
        navController = navController,
    )
}
