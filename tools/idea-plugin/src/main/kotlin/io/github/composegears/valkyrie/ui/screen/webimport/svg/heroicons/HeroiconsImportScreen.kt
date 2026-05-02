package io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons

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
import io.github.composegears.valkyrie.ui.screen.webimport.svg.common.SvgImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.svg.heroicons.di.HeroiconsModule
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val HeroiconsImportScreen by navDestination {
    val navController = navController()
    SvgImportScreen(
        title = stringResource("web.import.title.heroicons"),
        provider = inject(HeroiconsModule.heroiconsUseCase),
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
private fun HeroiconsImportScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(HeroiconsImportScreen)
}
