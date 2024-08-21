package io.github.composegears.valkyrie.ui.foundation.highlights

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.PhraseLocation
import dev.snipme.highlights.model.SyntaxLanguage
import dev.snipme.highlights.model.SyntaxThemes
import io.github.composegears.valkyrie.ui.foundation.theme.isLight

@Composable
fun rememberCodeHighlight(
  codeBlock: String,
  vararg emphasisLocation: PhraseLocation,
): Highlights {
  val isLight = MaterialTheme.colorScheme.isLight

  return remember(isLight, codeBlock) {
    Highlights.Builder()
      .code(codeBlock)
      .language(SyntaxLanguage.KOTLIN)
      .theme(SyntaxThemes.darcula(darkMode = !isLight))
      .emphasis(*emphasisLocation)
      .build()
  }
}

fun getEmphasisLocations(
  codeBlock: String,
  highlightText: String,
): Array<PhraseLocation> {
  val indexes = allIndexesOf(substring = highlightText, text = codeBlock)

  return Array(indexes.size) {
    PhraseLocation(
      start = indexes[it],
      end = indexes[it] + highlightText.length,
    )
  }
}

private fun allIndexesOf(substring: String, text: String): List<Int> {
  return buildList {
    var index = text.indexOf(substring)
    while (index >= 0) {
      add(index)
      index = text.indexOf(substring, index + 1)
    }
  }
}
