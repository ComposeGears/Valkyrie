package io.github.composegears.valkyrie.ui.screen.webimport

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.composegears.tiamat.compose.Navigation
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigationSlideInOut
import com.composegears.tiamat.compose.rememberNavController
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.BootstrapImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.LucideImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.material.MaterialSymbolsImportScreen

val WebImportFlow by navDestination {
    Navigation(
        modifier = Modifier.fillMaxSize(),
        navController = rememberNavController(
            startDestination = WebImportSelectorScreen,
        ),
        destinations = arrayOf(
            WebImportSelectorScreen,
            MaterialSymbolsImportScreen,
            LucideImportScreen,
            BootstrapImportScreen,
        ),
        contentTransformProvider = { isForward -> navigationSlideInOut(isForward) },
    )
}
