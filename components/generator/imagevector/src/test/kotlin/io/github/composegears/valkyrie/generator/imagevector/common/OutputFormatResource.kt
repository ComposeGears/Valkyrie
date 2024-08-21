package io.github.composegears.valkyrie.generator.imagevector.common

import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyProperty

fun OutputFormat.toResourceText(
  pathToBackingProperty: String,
  pathToLazyProperty: String,
): String = when (this) {
  BackingProperty -> getResourceText(pathToBackingProperty)
  LazyProperty -> getResourceText(pathToLazyProperty)
}
