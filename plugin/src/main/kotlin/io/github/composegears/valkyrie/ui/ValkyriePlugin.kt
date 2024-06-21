package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import org.koin.compose.koinInject

@Composable
fun ValkyriePlugin() {
    val inMemorySettings = koinInject<InMemorySettings>()

    val navController = rememberNavController(
        destinations = arrayOf(IntroScreen, ConversionScreen, SettingsScreen),
        startDestination = null,
        configuration = {
            if (current != null) return@rememberNavController

            val settingsService = inMemorySettings.settings.value
            val screen = when {
                settingsService.isFirstLaunch -> IntroScreen
                else -> ConversionScreen
            }
            navigate(screen)
        }
    )

    Navigation(
        modifier = Modifier.fillMaxSize(),
        navController = navController
    )
}