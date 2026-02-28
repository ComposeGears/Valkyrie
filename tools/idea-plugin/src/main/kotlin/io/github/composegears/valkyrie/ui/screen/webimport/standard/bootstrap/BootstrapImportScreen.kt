package io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap

import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.di.BootstrapModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.StandardImportScreen
import io.github.composegears.valkyrie.util.stringResource

val BootstrapImportScreen by navDestination {
    val navController = navController()
    StandardImportScreen(
        title = stringResource("web.import.title.bootstrap"),
        provider = inject(BootstrapModule.bootstrapUseCase),
        onBack = navController::back,
        onIconDownload = {
            navController.parent?.navigate(
                dest = SimpleConversionScreen,
                navArgs = TextSource(
                    name = it.name,
                    text = it.svgContent,
                ),
            )
        },
    )
}
