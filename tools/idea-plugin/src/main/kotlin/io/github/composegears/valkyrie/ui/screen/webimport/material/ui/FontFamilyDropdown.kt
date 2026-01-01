package io.github.composegears.valkyrie.ui.screen.webimport.material.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.composegears.valkyrie.ui.foundation.DropdownMenu
import io.github.composegears.valkyrie.ui.foundation.theme.PreviewTheme
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.model.font.IconFontFamily

@Composable
fun FontFamilyDropdown(
    fontFamily: IconFontFamily,
    modifier: Modifier = Modifier,
    onSelectFontFamily: (IconFontFamily) -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        current = fontFamily.displayName,
        values = IconFontFamily.entries.map { it.displayName },
        onSelect = { selectedName ->
            val selectedFontFamily = IconFontFamily.entries.first { it.displayName == selectedName }
            onSelectFontFamily(selectedFontFamily)
        },
    )
}

@Preview
@Composable
private fun FontFamilyDropdownPreview() = PreviewTheme {
    FontFamilyDropdown(
        fontFamily = IconFontFamily.OUTLINED,
        onSelectFontFamily = {},
    )
}
