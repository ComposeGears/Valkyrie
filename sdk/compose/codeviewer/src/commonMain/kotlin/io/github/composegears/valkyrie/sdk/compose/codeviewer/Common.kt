package io.github.composegears.valkyrie.sdk.compose.codeviewer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.PhraseLocation
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes

@Composable
fun rememberCodeHighlight(
    codeBlock: String,
    isLight: Boolean,
    vararg emphasisLocation: PhraseLocation,
): Highlights {
    return remember(isLight, codeBlock, emphasisLocation) {
        Highlights.Builder()
            .code(codeBlock)
            .language(SyntaxLanguage.KOTLIN)
            .theme(SyntaxThemes.darcula(darkMode = !isLight))
            .emphasis(*emphasisLocation)
            .build()
    }
}
