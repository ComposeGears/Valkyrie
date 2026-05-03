package io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome

import androidx.compose.runtime.Composable
import com.composegears.leviathan.compose.inject
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionParamsSource.TextSource
import io.github.composegears.valkyrie.ui.screen.mode.simple.conversion.SimpleConversionScreen
import io.github.composegears.valkyrie.ui.screen.webimport.common.StandardImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.di.FontAwesomeModule
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val FontAwesomeImportScreen by navDestination {
    val navController = navController()
    StandardImportScreen(
        title = stringResource("web.import.title.fontawesome"),
        provider = inject(FontAwesomeModule.fontAwesomeUseCase),
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

@Preview
@Composable
private fun FontAwesomeImportScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(FontAwesomeImportScreen)
}
