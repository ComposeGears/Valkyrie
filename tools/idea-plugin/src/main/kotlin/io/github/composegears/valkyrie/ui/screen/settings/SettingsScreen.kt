package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.composegears.tiamat.rememberNavController
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
            destinations = tabScreens,
        )

        ScrollableTabRow(
            tabs = tabNames,
            selectedTabIndex = tabScreens.indexOf(tabsNavController.current),
        ) { tabIndex ->
            val navDestination = tabScreens[tabIndex]
            if (navDestination != tabsNavController.current) {
                tabsNavController.replace(navDestination)
            }
        }

        Navigation(
            modifier = Modifier.fillMaxSize(),
            navController = tabsNavController,
        )
    }
}

private data class TabItem(
    val name: String,
    val screen: NavDestination<*>,
)
