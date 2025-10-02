package io.github.composegears.valkyrie.ui.screen.webimport

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.colored.GoogleMaterialLogo
import io.github.composegears.valkyrie.compose.ui.InfoCard
import io.github.composegears.valkyrie.ui.foundation.AppBarTitle
import io.github.composegears.valkyrie.ui.foundation.BackAction
import io.github.composegears.valkyrie.ui.foundation.TopAppBar
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource

val WebImportSelectorScreen by navDestination<Unit> {
    val navController = navController()

    WebImportSelectorScreenUI(
        onBack = { navController.back(transition = navigationSlideInOut(false)) },
    )
}

@Composable
private fun WebImportSelectorScreenUI(onBack: () -> Unit) {
    Column {
        TopAppBar {
            BackAction(onBack = onBack)
            AppBarTitle(title = stringResource("web.import.selector.title"))
        }
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .align(Alignment.CenterHorizontally)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            InfoCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { },
                image = ValkyrieIcons.Colored.GoogleMaterialLogo,
                title = stringResource("web.import.selector.google.title"),
                description = stringResource("web.import.selector.google.description"),
            )
            VerticalSpacer(16.dp)
            Text(
                text = stringResource("web.import.selector.coming.soon"),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun WebImportScreenPreview() = PreviewTheme(alignment = Alignment.TopCenter) {
    WebImportSelectorScreenUI(onBack = {})
}
