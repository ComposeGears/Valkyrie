package io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler

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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.StandardImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.di.TablerModule
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val TablerImportScreen by navDestination {
    val navController = navController()
    StandardImportScreen(
        title = stringResource("web.import.title.tabler"),
        provider = inject(TablerModule.tablerUseCase),
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
private fun TablerImportScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(TablerImportScreen)
}
