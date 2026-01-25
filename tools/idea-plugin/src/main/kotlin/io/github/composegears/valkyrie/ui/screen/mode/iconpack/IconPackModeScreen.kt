package io.github.composegears.valkyrie.ui.screen.mode.iconpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.InfoCard
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ExistingPackScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.NewPackScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer
import org.jetbrains.jewel.ui.icons.AllIconsKeys

val IconPackModeScreen by navDestination {
    val navController = navController()

    IconPackModeSetupContent(
        onBack = navController::back,
        onNewPack = {
            navController.navigate(NewPackScreen)
        },
        onExistingPack = {
            navController.navigate(ExistingPackScreen)
        },
    )
}

@Composable
private fun IconPackModeSetupContent(
    onBack: () -> Unit,
    onNewPack: () -> Unit,
    onExistingPack: () -> Unit,
) {
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("iconpack.mode.title"))
        }
        VerticallyScrollableContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                InfoCard(
                    key = AllIconsKeys.Actions.OpenNewTab,
                    iconSize = 34.dp,
                    onClick = onNewPack,
                    title = stringResource("iconpack.mode.newpack.title"),
                    description = stringResource("iconpack.mode.newpack.description"),
                )
                Spacer(16.dp)
                InfoCard(
                    key = AllIconsKeys.Toolwindows.ToolWindowComponents,
                    iconSize = 30.dp,
                    onClick = onExistingPack,
                    title = stringResource("iconpack.mode.existingpack.title"),
                    description = stringResource("iconpack.mode.existingpack.description"),
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconPackModeScreenPreview() = PreviewTheme {
    IconPackModeSetupContent(
        onBack = {},
        onNewPack = {},
        onExistingPack = {},
    )
}
