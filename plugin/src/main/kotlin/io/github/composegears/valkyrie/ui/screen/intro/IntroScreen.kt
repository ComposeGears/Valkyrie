package io.github.composegears.valkyrie.ui.screen.intro

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.NavDestination
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import com.composegears.tiamat.navigationSlideInOut
import io.github.composegears.valkyrie.ui.domain.model.Mode
import io.github.composegears.valkyrie.ui.domain.model.Mode.IconPack
import io.github.composegears.valkyrie.ui.domain.model.Mode.Simple
import io.github.composegears.valkyrie.ui.domain.model.Mode.Unspecified
import io.github.composegears.valkyrie.ui.foundation.HorizontalSpacer
import io.github.composegears.valkyrie.ui.foundation.TooltipButton
import io.github.composegears.valkyrie.ui.foundation.VerticalSpacer
import io.github.composegears.valkyrie.ui.screen.mode.iconpack.destination.IconPackDestinationScreen
import io.github.composegears.valkyrie.ui.screen.mode.simple.setup.SimpleModeSetupScreen

val IntroScreen: NavDestination<Unit> by navDestination {

    val navController = navController()

    IntroScreenUI(
        onSelect = {
            when (it) {
                Simple -> {
                    navController.navigate(
                        dest = SimpleModeSetupScreen,
                        transition = navigationSlideInOut(true)
                    )
                }
                IconPack -> {
                    navController.navigate(
                        dest = IconPackDestinationScreen,
                        transition = navigationSlideInOut(true)
                    )
                }
                Unspecified -> {}
            }
        }
    )
}

@Composable
@Preview
private fun IntroScreenUI(onSelect: (Mode) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Valkyrie",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )
        VerticalSpacer(42.dp)
        Text(
            text = "Choose conversion mode",
            style = MaterialTheme.typography.labelSmall,
            color = LocalContentColor.current.copy(alpha = 0.5f),
            textAlign = TextAlign.Center
        )
        VerticalSpacer(8.dp)

        ModeRow(
            title = "Simple",
            tooltipText = "One-click conversion from SVG/XML into ImageVector",
            onSelect = { onSelect(Simple) }
        )
        VerticalSpacer(16.dp)
        ModeRow(
            title = "IconPack",
            tooltipText = "Create organized icon pack with an extension property of you pack object and batch export into your project",
            onSelect = { onSelect(IconPack) }
        )
    }
}

@Composable
private fun ModeRow(
    title: String,
    tooltipText: String,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        HorizontalSpacer(36.dp)
        Button(onClick = onSelect) {
            Text(text = title)
        }
        TooltipButton(
            text = tooltipText,
            modifier = Modifier.widthIn(max = 250.dp)
        )
    }
}

@Preview
@Composable
private fun IntroScreenUIPreview() {
    IntroScreenUI(
        onSelect = {}
    )
}
