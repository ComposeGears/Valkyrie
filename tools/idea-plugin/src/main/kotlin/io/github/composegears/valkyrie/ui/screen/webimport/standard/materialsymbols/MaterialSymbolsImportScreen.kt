package io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.di.MaterialSymbolsModule
import io.github.composegears.valkyrie.ui.screen.webimport.standard.materialsymbols.ui.MaterialFontCustomization
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val MaterialSymbolsImportScreen by navDestination {
    val navController = navController()
    val parentNavController = navController.parent

    val useCase = inject(MaterialSymbolsModule.materialSymbolsConfigUseCase)

    StandardImportScreen(
        title = stringResource("web.import.title.material"),
        provider = useCase,
        onBack = navController::back,
        onIconDownload = { event ->
            parentNavController?.navigate(
                dest = SimpleConversionScreen,
                navArgs = TextSource(
                    name = event.name,
                    text = event.svgContent,
                ),
            )
        },
        customizationContent = { onClose ->
            val fontSettings by useCase.fontSettingsFlow.collectAsState()

            MaterialFontCustomization(
                fontSettings = fontSettings,
                onClose = onClose,
                onSettingsChange = useCase::updateFontSettings,
            )
        },
    )
}

@Preview
@Composable
private fun MaterialSymbolsImportScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(MaterialSymbolsImportScreen)
}
