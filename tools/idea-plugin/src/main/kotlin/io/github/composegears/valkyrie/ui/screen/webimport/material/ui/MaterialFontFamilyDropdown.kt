package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.DropdownList
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.MaterialIconFontFamily
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MaterialFontFamilyDropdown(
    fontFamily: MaterialIconFontFamily,
    modifier: Modifier = Modifier,
    onSelectFontFamily: (MaterialIconFontFamily) -> Unit,
) {
    DropdownList(
        modifier = modifier,
        items = MaterialIconFontFamily.entries,
        selected = fontFamily,
        transform = { it.displayName },
        onSelectItem = onSelectFontFamily,
    )
}

@Preview
@Composable
private fun MaterialFontFamilyDropdownPreview() = PreviewTheme(alignment = Alignment.Center) {
    var fontFamily by rememberMutableState { MaterialIconFontFamily.Outlined }

    MaterialFontFamilyDropdown(
        modifier = Modifier.width(100.dp),
        fontFamily = fontFamily,
        onSelectFontFamily = { fontFamily = it },
    )
}
