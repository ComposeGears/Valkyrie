package io.github.composegears.valkyrie.ui.screen.mode.iconpack

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.screen.conversion.ConversionScreen

val IconPackPreviewScreen by navDestination<Unit> {
    val navController = navController()

    IconPackPreviewScreenUI(
        onBack = {
            navController.back(transition = navigationSlideInOut(false))
        },
        onNext = {
            navController.editBackStack { clear() }
            navController.navigate(
                dest = ConversionScreen,
                transition = navigationSlideInOut(true)
            )
        }
    )
}

@Composable
fun IconPackPreviewScreenUI(
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = "Icon Pack Preview")
        }
        Button(
            modifier = Modifier
                .align(Alignment.End),
            onClick = onNext,
        ) {
            Text(text = "Next")
        }
    }
}