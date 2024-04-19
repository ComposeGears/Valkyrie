package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.settings.ValkyrieSettings
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen

@Composable
fun ValkyriePlugin() {
    val navController = rememberNavController(
        destinations = arrayOf(IntroScreen, ConversionScreen),
        startDestination = null,
        configuration = {
            if (current != null) return@rememberNavController

            val settingsService = ValkyrieSettings.instance
            val screen = when {
                settingsService.isFirstStart -> IntroScreen
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