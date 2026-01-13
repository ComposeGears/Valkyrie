package io.github.composegears.valkyrie.ui.screen.mode.imagevectortoxml.conversion.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import io.github.composegears.valkyrie.compose.util.isLight
import io.github.composegears.valkyrie.sdk.compose.codeviewer.CodeEditor
import io.github.composegears.valkyrie.sdk.compose.codeviewer.rememberCodeHighlight

@Composable
fun XmlCodeViewer(
    text: String,
    modifier: Modifier = Modifier,
    onChange: (String) -> Unit = {},
) {
    val highlights = rememberCodeHighlight(
        codeBlock = text,
        isLight = MaterialTheme.colorScheme.isLight,
    )

    CodeEditor(
        modifier = modifier,
        highlights = highlights,
        onValueChange = onChange,
        textStyle = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp, fontSize = 13.sp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
        ),
    )
}
