package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination.IconPackDestinationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.SimpleModeSetupScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun ValkyriePlugin() {
    val inMemorySettings = koinInject<InMemorySettings>()

    val navController = rememberNavController(
        destinations = arrayOf(
            IntroScreen,
            SimpleModeSetupScreen,
            SimpleConversionScreen,

            IconPackDestinationScreen,
            IconPackCreationScreen,
            IconPackConversionScreen,

            SettingsScreen
        ),
        startDestination = null,
        configuration = {
            if (current != null) return@rememberNavController

            val settings = inMemorySettings.current
            val screen = when (settings.mode) {
                Simple -> SimpleConversionScreen
                IconPack -> IconPackConversionScreen
                Unspecified -> IntroScreen
            }
            navigate(screen)
        }
    )

    Navigation(
        modifier = Modifier.fillMaxSize(),
        navController = navController
    )
}