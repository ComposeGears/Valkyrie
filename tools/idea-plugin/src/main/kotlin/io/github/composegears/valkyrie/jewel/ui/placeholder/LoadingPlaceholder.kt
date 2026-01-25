package io.github.composegears.valkyrie.jewel.ui.placeholder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.util.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.jewel.ui.component.CircularProgressIndicatorBig
import org.jetbrains.jewel.ui.component.Text

@Composable
fun LoadingPlaceholder(
    modifier: Modifier = Modifier,
    text: String = stringResource("component.placeholder.loader"),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        CircularProgressIndicatorBig()
        Text(text = text)
    }
}

@Preview
@Composable
private fun LoadingPlaceholderPreview() = PreviewTheme(alignment = Alignment.Center) {
    LoadingPlaceholder()
}
