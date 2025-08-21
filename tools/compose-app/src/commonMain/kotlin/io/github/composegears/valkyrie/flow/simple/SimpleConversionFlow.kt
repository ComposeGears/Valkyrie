package io.github.composegears.valkyrie.flow.simple

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.rememberNavController
import io.github.composegears.valkyrie.flow.simple.screen.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.flow.simple.screen.picker.SimplePickerScreen

val SimpleConversionFlow by navDestination<Unit> {
    val navController = rememberNavController(
        startDestination = SimplePickerScreen,
    )

    Navigation(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        destinations = arrayOf(SimplePickerScreen, SimpleConversionScreen),
    )
}
