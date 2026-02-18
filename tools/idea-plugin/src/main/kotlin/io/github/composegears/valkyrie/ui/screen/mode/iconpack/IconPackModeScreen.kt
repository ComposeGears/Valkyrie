package io.github.composegears.valkyrie.ui.screen.mode.iconpack

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.HorizontalDivider
import io.github.composegears.valkyrie.jewel.InfoCard
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.existingpack.ExistingPackScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.material.MaterialPackScreen
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.newpack.NewPackScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
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
        onMaterialPack = {
            navController.navigate(MaterialPackScreen)
        },
    )
}

@Composable
private fun IconPackModeSetupContent(
    onBack: () -> Unit,
    onNewPack: () -> Unit,
    onExistingPack: () -> Unit,
    onMaterialPack: () -> Unit,
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
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoCard(
                    key = AllIconsKeys.Actions.OpenNewTab,
                    iconSize = 34.dp,
                    onClick = onNewPack,
                    title = stringResource("iconpack.mode.new.pack.title"),
                    description = stringResource("iconpack.mode.new.pack.description"),
                )
                InfoCard(
                    key = AllIconsKeys.Toolwindows.ToolWindowComponents,
                    iconSize = 30.dp,
                    onClick = onExistingPack,
                    title = stringResource("iconpack.mode.existing.pack.title"),
                    description = stringResource("iconpack.mode.existing.pack.description"),
                )
                Spacer(8.dp)
                HorizontalDivider(modifier = Modifier.width(72.dp))
                Spacer(8.dp)
                InfoCard(
                    key = AllIconsKeys.FileTypes.SourceMap,
                    iconSize = 30.dp,
                    onClick = onMaterialPack,
                    title = stringResource("iconpack.mode.material.pack.title"),
                    description = stringResource("iconpack.mode.material.pack.description"),
                )
            }
        }
    }
}

@Preview
@Composable
private fun IconPackModeScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(IconPackModeScreen)
}
