package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.currentNavDestinationAsState
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigationSlideInOut
import com.composegears.tiamat.compose.rememberNavController
import com.composegears.tiamat.compose.replace
import com.composegears.tiamat.navigation.NavDestination
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.ScrollableTabRow
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.screen.settings.tabs.AboutSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.GeneralSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.export.ImageVectorExportSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.preview.ImageVectorPreviewSettingsScreen

val SettingsScreen by navDestination<Unit> {
    val navController = navController()

    val tabs = remember {
        listOf(
            TabItem(name = "General", screen = GeneralSettingsScreen),
            TabItem(name = "Export", screen = ImageVectorExportSettingsScreen),
            TabItem(name = "Preview", screen = ImageVectorPreviewSettingsScreen),
            TabItem(name = "About", screen = AboutSettingsScreen),
        )
    }
    val tabNames = remember { tabs.map { it.name } }
    val tabScreens = remember { tabs.map { it.screen }.toTypedArray() }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(
                onBack = {
                    navController.back(transition = navigationSlideInOut(false))
                },
            )
            AppBarTitle("Settings")
        }

        val tabsNavController = rememberNavController(
            startDestination = GeneralSettingsScreen,
        )
        val tabsDestination by tabsNavController.currentNavDestinationAsState()

        ScrollableTabRow(
            tabs = tabNames,
            selectedTabIndex = tabScreens.indexOfFirst { destination -> destination.name == tabsDestination?.name },
        ) { tabIndex ->
            val navDestination = tabScreens[tabIndex]
            if (navDestination != tabsDestination) {
                tabsNavController.replace(navDestination)
            }
        }

        Navigation(
            modifier = Modifier.fillMaxSize(),
            navController = tabsNavController,
            destinations = tabScreens,
        )
    }
}

private data class TabItem(
    val name: String,
    val screen: NavDestination<*>,
)
