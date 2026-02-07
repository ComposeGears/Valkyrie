package io.github.composegears.valkyrie.ui.screen.webimport

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.compose.back
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.colored.GoogleMaterialLogo
import io.github.composegears.valkyrie.jewel.BackAction
import io.github.composegears.valkyrie.jewel.InfoCard
import io.github.composegears.valkyrie.jewel.Title
import io.github.composegears.valkyrie.jewel.Toolbar
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.ui.screen.webimport.IconProviders.GoogleMaterialSymbols
import io.github.composegears.valkyrie.ui.screen.webimport.material.MaterialSymbolsImportScreen
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.InfoText
import org.jetbrains.jewel.ui.component.VerticallyScrollableContainer

val WebImportSelectorScreen by navDestination {
    val navController = navController()

    WebImportSelectorScreenUI(
        onBack = navController::back,
        onClick = {
            val screen = when (it) {
                GoogleMaterialSymbols -> MaterialSymbolsImportScreen
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
            ) {
                InfoCard(
                    onClick = { onClick(GoogleMaterialSymbols) },
                    icon = ValkyrieIcons.Colored.GoogleMaterialLogo,
                    title = stringResource("web.import.selector.google.title"),
                    description = stringResource("web.import.selector.google.description"),
                    tint = Color.Unspecified,
                )
                Spacer(16.dp)
                InfoText(
                    text = stringResource("web.import.selector.coming.soon"),
                    maxLines = 2,
                )
            }
        }
    }
}

enum class IconProviders {
    GoogleMaterialSymbols,
}

@Preview
@Composable
private fun WebImportScreenPreview() = PreviewTheme {
    WebImportSelectorScreenUI(onBack = {}, onClick = {})
}
