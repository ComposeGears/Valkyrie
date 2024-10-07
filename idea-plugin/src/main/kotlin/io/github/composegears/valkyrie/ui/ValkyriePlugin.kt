package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.theme.LocalProject
import io.github.composegears.valkyrie.ui.screen.intro.IntroScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.conversion.IconPackConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation.IconPackCreationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupScreen
import io.github.composegears.valkyrie.ui.screen.preview.CodePreviewScreen
import io.github.composegears.valkyrie.ui.screen.settings.SettingsScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

@Composable
fun ValkyriePlugin(
    modifier: Modifier = Modifier,
) {
    val inMemorySettings = koinInject<InMemorySettings>()
    val project = LocalProject.current

    val navController = rememberNavController(
        destinations = arrayOf(
            IntroScreen,
            SimpleModeSetupScreen,
            SimpleConversionScreen,

            IconPackCreationScreen,
            IconPackConversionScreen,

            CodePreviewScreen,

            SettingsScreen,
        ),
        startDestination = null,
        configuration = {
            if (current != null) return@rememberNavController

            val uiState = inMemorySettings.uiState
            if (uiState.isNotEmpty()) {
                runCatching {
                    loadFromSavedState(uiState)
                }
            }
            if (current != null) return@rememberNavController

            val settings = inMemorySettings.current
            val screen = when (settings.mode) {
                Simple -> SimpleConversionScreen
                IconPack -> IconPackConversionScreen
                Unspecified -> IntroScreen
            }
            editBackStack {
                add(IntroScreen)
            }
            navigate(screen)
        },
    )

    LaunchedEffect(Unit) {
        val globalEventsHandler = project.globalEventsHandler

        globalEventsHandler
            .events
            .onEach { event ->
                when (event) {
                    is PluginEvents.ImportIcons -> {
                        globalEventsHandler.resetCache()

                        navController.editBackStack {
                            clear()
                            add(IntroScreen)
                        }
                        navController.replace(
                            dest = IconPackConversionScreen,
                            navArgs = event.paths,
                        )
                    }
                }
            }
            .launchIn(this)
    }

    DisposableEffect(Unit) {
        onDispose {
            inMemorySettings.updateUIState(navController.getSavedState())
        }
    }

    Navigation(
        modifier = modifier.fillMaxSize(),
        navController = navController,
    )
}
