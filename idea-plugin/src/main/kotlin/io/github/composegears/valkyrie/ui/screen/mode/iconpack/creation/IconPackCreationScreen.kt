package io.github.composegears.valkyrie.ui.screen.mode.iconpack.creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavController
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import com.composegears.tiamat.rememberNavController
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.SegmentedButton
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ExistingPackScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.NewPackScreen

val IconPackCreationScreen by navDestination<Unit> {
    val nestedNavController = rememberNavController(
        startDestination = NewPackScreen,
        destinations = arrayOf(NewPackScreen, ExistingPackScreen),
    )

    IconPackModeSetupUI(
        nestedNavController = nestedNavController,
        onBack = {
            if (nestedNavController.canGoBack) {
                nestedNavController.back()
            } else {
                nestedNavController.parent?.back(transition = navigationSlideInOut(false))
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun IconPackModeSetupUI(
    nestedNavController: NavController,
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
                .padding(16.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .height(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val modes = remember { listOf(PackMode.NEW, PackMode.EXISTING) }
            var currentPackMode by rememberSaveable { mutableStateOf(PackMode.NEW) }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                modes.forEachIndexed { index, mode ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = modes.size),
                        onClick = {
                            currentPackMode = mode
                            val screen = when (mode) {
                                PackMode.NEW -> NewPackScreen
                                PackMode.EXISTING -> ExistingPackScreen
                            }
                            nestedNavController.popToTop(dest = screen)
                        },
                        selected = currentPackMode == mode,
                        label = {
                            Text(mode.title)
                        },
                    )
                }
            }
            VerticalSpacer(36.dp)

            Navigation(
                modifier = Modifier.fillMaxSize(),
                navController = nestedNavController,
            )
        }
    }
}

enum class PackMode(val title: String) {
    NEW(title = "Create new"),
    EXISTING(title = "Existing pack"),
}
