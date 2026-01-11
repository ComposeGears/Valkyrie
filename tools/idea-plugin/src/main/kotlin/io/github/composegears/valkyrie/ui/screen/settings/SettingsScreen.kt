package io.github.composegears.valkyrie.ui.screen.settings

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.currentNavDestinationAsState
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.rememberNavController
import com.composegears.tiamat.compose.replace
import com.composegears.tiamat.navigation.NavDestination
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.screen.settings.tabs.AboutSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.GeneralSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.generator.GeneratorSettingsScreen
import io.github.composegears.valkyrie.ui.screen.settings.tabs.preview.ImageVectorPreviewSettingsScreen
import io.github.composegears.valkyrie.uikit.HorizontalDivider
import io.github.composegears.valkyrie.uikit.tooling.PreviewTheme
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.SimpleTabContent
import org.jetbrains.jewel.ui.component.TabData
import org.jetbrains.jewel.ui.component.TabStrip
import org.jetbrains.jewel.ui.theme.defaultTabStyle

val SettingsScreen by navDestination<Unit> {
    val navController = navController()

    val tabs = remember {
        listOf(
            TabItem(name = "General", screen = GeneralSettingsScreen),
            TabItem(name = "Generator", screen = GeneratorSettingsScreen),
            TabItem(name = "Preview", screen = ImageVectorPreviewSettingsScreen),
            TabItem(name = "About", screen = AboutSettingsScreen),
        )
    }
    val tabNames = remember { tabs.map { it.name } }
    val tabScreens = remember { tabs.map { it.screen }.toTypedArray() }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = navController::back)
            AppBarTitle("Settings")
        }

        val tabsNavController = rememberNavController(
            startDestination = GeneralSettingsScreen,
        )
        val tabsDestination by tabsNavController.currentNavDestinationAsState()

        Tabs(
            tabNames = tabNames,
            selectedTabIndex = { tabScreens.indexOfFirst { destination -> destination.name == tabsDestination?.name } },
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

@Composable
private fun Tabs(
    tabNames: List<String>,
    selectedTabIndex: () -> Int,
    onTabClick: (Int) -> Unit,
) {
    Column {
        TabStrip(
            tabs = tabNames.mapIndexed { index, string ->
                TabData.Default(
                    selected = index == selectedTabIndex(),
                    content = {
                        SimpleTabContent(label = string, state = it)
                    },
                    onClick = { onTabClick(index) },
                    closable = false,
                )
            },
            style = JewelTheme.defaultTabStyle,
            interactionSource = remember { MutableInteractionSource() },
        )
        HorizontalDivider()
    }
}

private data class TabItem(
    val name: String,
    val screen: NavDestination<*>,
)

@Preview
@Composable
internal fun SettingsScreenPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    var selectedTabIndex by rememberMutableState { 0 }
    val tabNames = listOf("General", "Generator", "Preview", "About")

    Tabs(
        tabNames = tabNames,
        selectedTabIndex = { selectedTabIndex },
        onTabClick = { selectedTabIndex = it },
    )
}
