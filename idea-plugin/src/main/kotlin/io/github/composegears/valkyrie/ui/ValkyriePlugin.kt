package io.github.composegears.valkyrie.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.NavController
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.navigationNone
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.service.GlobalEventsHandler.Companion.globalEventsHandler
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.ImportIcons
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.RefreshPlugin
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PluginEvents.SetupIconPackMode
import io.github.composegears.valkyrie.settings.InMemorySettings
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.LocalSnackBar
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

            initialFlow(inMemorySettings)
        },
    )

    LaunchedEffect(Unit) {
        val globalEventsHandler = project.globalEventsHandler

        globalEventsHandler
            .events
            .onEach { event ->
                when (event) {
                    is ImportIcons -> navController.openConversionFlow(event)
                    is SetupIconPackMode -> navController.openSetupIconPackWithPendingData(event)
                    is RefreshPlugin -> navController.initialFlow(inMemorySettings)
                }.also {
                    globalEventsHandler.resetCache()
                }
            }
            .launchIn(this)
    }

    DisposableEffect(Unit) {
        onDispose {
            inMemorySettings.updateUIState(navController.getSavedState())
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Navigation(
            modifier = Modifier.matchParentSize(),
            navController = navController,
        )
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = LocalSnackBar.current,
            snackbar = {
                Snackbar(
                    snackbarData = it,
                    shape = MaterialTheme.shapes.small,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                )
            },
        )
    }
}

private fun NavController.initialFlow(inMemorySettings: InMemorySettings) {
    val settings = inMemorySettings.current
    val screen = when (settings.mode) {
        Simple -> SimpleConversionScreen
        IconPack -> IconPackConversionScreen
        Unspecified -> IntroScreen
    }

    if (current != screen) {
        editBackStack {
            add(IntroScreen)
        }
        replace(screen)
    }
}

private fun NavController.openConversionFlow(event: ImportIcons) {
    when (current) {
        IconPackConversionScreen -> {
            replace(
                dest = IconPackConversionScreen,
                navArgs = event.pathData,
                transition = navigationNone(),
            )
        }
        else -> {
            editBackStack {
                clear()
                add(IntroScreen)
            }
            replace(
                dest = IconPackConversionScreen,
                navArgs = event.pathData,
            )
        }
    }
}

private fun NavController.openSetupIconPackWithPendingData(event: SetupIconPackMode) {
    when (current) {
        IconPackCreationScreen -> {
            replace(
                dest = IconPackCreationScreen,
                navArgs = event.pathData,
                transition = navigationNone(),
            )
        }
        else -> {
            editBackStack {
                clear()
                add(IntroScreen)
            }
            replace(
                dest = IconPackCreationScreen,
                navArgs = event.pathData,
            )
        }
    }
}
