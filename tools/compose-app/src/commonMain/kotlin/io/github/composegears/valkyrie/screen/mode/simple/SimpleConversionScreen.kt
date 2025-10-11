package io.github.composegears.valkyrie.screen.mode.simple

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Back

val SimpleConversionScreen by navDestination<Unit> {
    val navController = navController()

    SimpleConversionUi(onBack = navController::back)
}

@Composable
private fun SimpleConversionUi(onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row {
            IconButton(onClick = onBack) {
                Icon(imageVector = ValkyrieIcons.Outlined.Back, contentDescription = null)
            }
        }
        WeightSpacer()
        Text(text = "Simple Conversion", modifier = Modifier.align(Alignment.CenterHorizontally))
        WeightSpacer()
    }
}
