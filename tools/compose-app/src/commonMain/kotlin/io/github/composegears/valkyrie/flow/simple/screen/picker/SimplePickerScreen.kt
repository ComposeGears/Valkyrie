package io.github.composegears.valkyrie.flow.simple.screen.picker

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.compose.core.clickableNoRipple
import io.github.composegears.valkyrie.compose.core.layout.CenterVerticalRow
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.idea.AddFile
import io.github.composegears.valkyrie.flow.simple.screen.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.flow.simple.screen.picker.ui.rememberIconPicker
import io.github.composegears.valkyrie.ui.AppBarTitle
import io.github.composegears.valkyrie.ui.BackAction
import io.github.composegears.valkyrie.ui.HoverBox
import io.github.composegears.valkyrie.ui.SharedTransitionScope
import io.github.composegears.valkyrie.ui.TopAppBar
import io.github.vinceglb.filekit.PlatformFile
import org.jetbrains.compose.resources.stringResource
import valkyrie.tools.compose_app.generated.resources.Res
import valkyrie.tools.compose_app.generated.resources.simple_conversion_browse
import valkyrie.tools.compose_app.generated.resources.simple_conversion_screen_title

val SimplePickerScreen by navDestination<Unit> {
    val navController = navController()

    SimplePickerUi(
        onPick = {
            navController.navigate(
                dest = SimpleConversionScreen,
                navArgs = it,
            )
        },
        onBack = navController::back,
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun AnimatedVisibilityScope.SimplePickerUi(
    onPick: (List<PlatformFile>) -> Unit,
    onBack: () -> Unit,
) {
    val iconPicker = rememberIconPicker { files ->
        if (files.isNotEmpty()) {
            onPick(files)
        }
    }

    SharedTransitionScope {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar {
                BackAction(onBack = onBack)
                AppBarTitle(
                    modifier = Modifier
                        .sharedElement(
                            sharedContentState = rememberSharedContentState("top-appbar"),
                            animatedVisibilityScope = this@SimplePickerUi,
                        )
                        .renderInSharedTransitionScopeOverlay(),
                    title = stringResource(Res.string.simple_conversion_screen_title),
                )
            }
            WeightSpacer(weight = 0.3f)
            HoverBox(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(500.dp)
                    .aspectRatio(1.5f)
                    .padding(32.dp)
                    .clickableNoRipple(onClick = iconPicker::launch),
            ) {
                CenterVerticalRow(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        imageVector = ValkyrieIcons.Idea.AddFile,
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier.padding(8.dp),
                        text = stringResource(Res.string.simple_conversion_browse),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }
            WeightSpacer(weight = 0.7f)
        }
    }
}
