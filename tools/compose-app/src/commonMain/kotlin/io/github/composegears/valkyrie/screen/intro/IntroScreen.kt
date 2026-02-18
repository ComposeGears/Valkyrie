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
import com.composegears.tiamat.compose.navController
import com.composegears.tiamat.compose.navDestination
import com.composegears.tiamat.compose.navigate
import io.github.composegears.valkyrie.screen.mode.simple.SimpleConversionScreen
import io.github.composegears.valkyrie.sdk.compose.foundation.dim
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.Spacer
import io.github.composegears.valkyrie.sdk.compose.foundation.layout.WeightSpacer
import io.github.composegears.valkyrie.sdk.compose.icons.ValkyrieIcons
import io.github.composegears.valkyrie.sdk.compose.icons.outlined.Conversion
import io.github.composegears.valkyrie.shared.ValkyrieMode
import io.github.composegears.valkyrie.ui.InfoCard
import org.jetbrains.compose.resources.stringResource
import valkyrie.tools.compose_app.generated.resources.Res
import valkyrie.tools.compose_app.generated.resources.intro_card_simple_description
import valkyrie.tools.compose_app.generated.resources.intro_card_simple_title
import valkyrie.tools.compose_app.generated.resources.intro_header
import valkyrie.tools.compose_app.generated.resources.intro_subheader

val IntroScreen by navDestination {
    val navController = navController()

    IntroUI(
        onModeChange = {
            when (it) {
                ValkyrieMode.Simple -> navController.navigate(SimpleConversionScreen)
                else -> {}
            }
        },
    )
}

@Composable
private fun IntroUI(onModeChange: (ValkyrieMode) -> Unit) {
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
        Spacer(42.dp)
        Text(
            text = stringResource(Res.string.intro_subheader),
            style = MaterialTheme.typography.labelSmall,
            color = LocalContentColor.current.dim(),
            textAlign = TextAlign.Center,
        )
        Spacer(8.dp)
        InfoCard(
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = { onModeChange(ValkyrieMode.Simple) },
            icon = ValkyrieIcons.Outlined.Conversion,
            title = stringResource(Res.string.intro_card_simple_title),
            description = stringResource(Res.string.intro_card_simple_description),
        )
        WeightSpacer(weight = 0.7f)
    }
}
