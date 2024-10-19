package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavController
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.navArgsOrNull
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.service.GlobalEventsHandler.PendingPathData
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.SegmentedButton
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ExistingPackScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.NewPackScreen

val IconPackCreationScreen by navDestination<PendingPathData> {
    val pendingData = navArgsOrNull()

    val nestedNavController = rememberNavController(
        startDestination = NewPackScreen,
        destinations = arrayOf(NewPackScreen, ExistingPackScreen),
        startDestinationNavArgs = pendingData,
    )

    IconPackModeSetupUI(
        tabsNavController = nestedNavController,
        pendingPathData = pendingData,
        onBack = {
            if (nestedNavController.canGoBack && nestedNavController.current != NewPackScreen) {
                nestedNavController.back()
            } else {
                nestedNavController.parent?.back(transition = navigationSlideInOut(false))
            }
        },
    )
}

@Composable
private fun IconPackModeSetupUI(
    tabsNavController: NavController,
    pendingPathData: PendingPathData?,
    onBack: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle("IconPack setup")
        }
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val tabs = remember { listOf(NewPackScreen, ExistingPackScreen) }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                tabs.forEachIndexed { index, tab ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = tabs.size),
                        onClick = {
                            tabsNavController.popToTop(
                                dest = tab,
                                orElse = {
                                    navigate(dest = tab, navArgs = pendingPathData)
                                },
                            )
                        },
                        selected = tabsNavController.current == tab,
                        label = {
                            when (tab) {
                                NewPackScreen -> Text(text = "Create new")
                                ExistingPackScreen -> Text(text = "Existing pack")
                            }
                        },
                    )
                }
            }
            VerticalSpacer(36.dp)

            Navigation(
                modifier = Modifier.fillMaxSize(),
                navController = tabsNavController,
            )
        }
    }
}
