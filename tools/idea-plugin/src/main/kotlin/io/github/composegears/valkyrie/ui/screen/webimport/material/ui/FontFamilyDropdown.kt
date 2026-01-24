package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.composegears.valkyrie.jewel.DropdownList
import io.github.composegears.valkyrie.jewel.tooling.PreviewTheme
import io.github.composegears.valkyrie.sdk.compose.foundation.rememberMutableState
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily

@Composable
fun FontFamilyDropdown(
    fontFamily: IconFontFamily,
    modifier: Modifier = Modifier,
    onSelectFontFamily: (IconFontFamily) -> Unit,
) {
    DropdownList(
        modifier = modifier,
        items = IconFontFamily.entries,
        selected = fontFamily,
        transform = { it.displayName },
        onSelectItem = onSelectFontFamily,
    )
}

@Preview
@Composable
private fun FontFamilyDropdownPreview() = PreviewTheme(alignment = Alignment.Center) {
    var fontFamily by rememberMutableState { IconFontFamily.OUTLINED }

    FontFamilyDropdown(
        modifier = Modifier.width(100.dp),
        fontFamily = fontFamily,
        onSelectFontFamily = { fontFamily = it },
    )
}
