package io.github.composegears.valkyrie.ui.foundation.highlights

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import io.github.composegears.valkyrie.ui.foundation.theme.isLight

@Composable
fun rememberCodeHighlight(code: String): Highlights {
    val isLight = MaterialTheme.colorScheme.isLight

    return remember(isLight) {
        Highlights.Builder()
            .code(code)
            .language(SyntaxLanguage.KOTLIN)
            .theme(SyntaxThemes.darcula(darkMode = !isLight))
            .build()
    }
}
