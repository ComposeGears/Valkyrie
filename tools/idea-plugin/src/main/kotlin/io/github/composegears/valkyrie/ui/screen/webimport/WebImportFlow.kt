package io.github.composegears.valkyrie.ui.screen.webimport

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.composegears.tiamat.Navigation
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.rememberNavController

val WebImportFlow by navDestination<Unit> {
    Navigation(
        modifier = Modifier.fillMaxSize(),
        navController = rememberNavController(
            destinations = arrayOf(WebImportSelectorScreen),
            startDestination = WebImportSelectorScreen,
        ),
    )
}
