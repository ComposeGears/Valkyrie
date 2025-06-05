package io.github.composegears.valkyrie.screen.intro

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composegears.tiamat.navController
import com.composegears.tiamat.navDestination
import io.github.composegears.valkyrie.compose.core.layout.VerticalSpacer
import io.github.composegears.valkyrie.compose.core.layout.WeightSpacer
import io.github.composegears.valkyrie.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.compose.icons.outlined.Conversion
import io.github.composegears.valkyrie.compose.ui.InfoCard
import io.github.composegears.valkyrie.compose.util.dim
import io.github.composegears.valkyrie.flow.simple.SimpleConversionFlow
import io.github.composegears.valkyrie.shared.Mode
import org.jetbrains.compose.resources.stringResource
import valkyrie.tools.compose_app.generated.resources.Res
import valkyrie.tools.compose_app.generated.resources.intro_card_simple_description
import valkyrie.tools.compose_app.generated.resources.intro_card_simple_title
import valkyrie.tools.compose_app.generated.resources.intro_header
import valkyrie.tools.compose_app.generated.resources.intro_subheader

val IntroScreen by navDestination<Unit> {
    val navController = navController()

    IntroUI(
        onModeChange = {
            when (it) {
                Mode.Simple -> navController.navigate(SimpleConversionFlow)
                else -> {}
            }
        },
    )
}

@Composable
private fun IntroUI(onModeChange: (Mode) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        WeightSpacer(weight = 0.3f)
        Text(
            text = stringResource(Res.string.intro_header),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        VerticalSpacer(42.dp)
        Text(
            text = stringResource(Res.string.intro_subheader),
            style = MaterialTheme.typography.labelSmall,
            color = LocalContentColor.current.dim(),
            textAlign = TextAlign.Center,
        )
        VerticalSpacer(8.dp)
        InfoCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { onModeChange(Mode.Simple) },
            image = ValkyrieIcons.Outlined.Conversion,
            title = stringResource(Res.string.intro_card_simple_title),
            description = stringResource(Res.string.intro_card_simple_description),
        )
        WeightSpacer(weight = 0.7f)
    }
}
