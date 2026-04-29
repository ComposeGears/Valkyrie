package io.github.composegears.valkyrie.ui.screen.webimport

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.TiamatPreview
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.InfoCard
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.ProjectPreviewTheme
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.icons.colored.BootstrapLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.BoxLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.EvaLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.FontAwesomeLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.GoogleMaterialLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.LucideLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.RemixLogo
import io.github.composegears.valkyrie.sdk.compose.icons.colored.TablerLogo
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.Bootstrap
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.BoxIcons
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.Eva
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.FontAwesome
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.GoogleMaterialSymbols
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.Lucide
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.Remix
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.Tabler
import io.github.composegears.valkyrie.ui.screen.webimport.material.MaterialSymbolsImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.bootstrap.BootstrapImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.boxicons.BoxIconsImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.eva.EvaImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.fontawesome.FontAwesomeImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.lucide.LucideImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.remix.RemixImportScreen
import io.github.composegears.valkyrie.ui.screen.webimport.standard.tabler.TablerImportScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val WebImportSelectorScreen by navDestination {
    val navController = navController()

    WebImportSelectorScreenUI(
        onBack = navController::back,
        onClick = {
            val screen = when (it) {
                GoogleMaterialSymbols -> MaterialSymbolsImportScreen
                Lucide -> LucideImportScreen
                Bootstrap -> BootstrapImportScreen
                Remix -> RemixImportScreen
                BoxIcons -> BoxIconsImportScreen
                FontAwesome -> FontAwesomeImportScreen
                Tabler -> TablerImportScreen
                Eva -> EvaImportScreen
            }

            navController.navigate(dest = screen)
        },
    )
}

@Composable
private fun WebImportSelectorScreenUI(
    onBack: () -> Unit,
    onClick: (IconProviders) -> Unit,
) {
    Column {
        Toolbar {
            BackAction(onBack = onBack)
            Title(text = stringResource("web.import.selector.title"))
        }
        VerticallyScrollableContainer(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .align(Alignment.TopCenter)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                IconProviders.entries.forEach { provider ->
                    InfoCard(
                        onClick = { onClick(provider) },
                        icon = provider.icon,
                        tint = Color.Unspecified,
                        title = stringResource(provider.titleKey),
                        description = stringResource(provider.descriptionKey),
                    )
                }
            }
        }
    }
}

enum class IconProviders(
    val icon: ImageVector,
    val titleKey: String,
    val descriptionKey: String,
) {
    Bootstrap(
        icon = ValkyrieIcons.Colored.BootstrapLogo,
        titleKey = "web.import.selector.bootstrap.title",
        descriptionKey = "web.import.selector.bootstrap.description",
    ),
    BoxIcons(
        icon = ValkyrieIcons.Colored.BoxLogo,
        titleKey = "web.import.selector.boxicons.title",
        descriptionKey = "web.import.selector.boxicons.description",
    ),
    Eva(
        icon = ValkyrieIcons.Colored.EvaLogo,
        titleKey = "web.import.selector.eva.title",
        descriptionKey = "web.import.selector.eva.description",
    ),
    FontAwesome(
        icon = ValkyrieIcons.Colored.FontAwesomeLogo,
        titleKey = "web.import.selector.fontawesome.title",
        descriptionKey = "web.import.selector.fontawesome.description",
    ),
    GoogleMaterialSymbols(
        icon = ValkyrieIcons.Colored.GoogleMaterialLogo,
        titleKey = "web.import.selector.google.title",
        descriptionKey = "web.import.selector.google.description",
    ),
    Lucide(
        icon = ValkyrieIcons.Colored.LucideLogo,
        titleKey = "web.import.selector.lucide.title",
        descriptionKey = "web.import.selector.lucide.description",
    ),
    Remix(
        icon = ValkyrieIcons.Colored.RemixLogo,
        titleKey = "web.import.selector.remix.title",
        descriptionKey = "web.import.selector.remix.description",
    ),
    Tabler(
        icon = ValkyrieIcons.Colored.TablerLogo,
        titleKey = "web.import.selector.tabler.title",
        descriptionKey = "web.import.selector.tabler.description",
    ),
}

@Preview
@Composable
private fun WebImportScreenPreview() = ProjectPreviewTheme {
    TiamatPreview(WebImportSelectorScreen)
}
