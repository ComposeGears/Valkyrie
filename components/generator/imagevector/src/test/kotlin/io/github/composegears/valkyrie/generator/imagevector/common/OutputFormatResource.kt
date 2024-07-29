package io.github.composegears.valkyrie.generator.imagevector.common

import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyDelegateProperty
import io.github.composegears.valkyrie.generator.imagevector.common.ResourceUtils.getResourceText

fun OutputFormat.toResourceText(
    pathToBackingProperty: String,
    pathToLazyProperty: String,
): String = when (this) {
    BackingProperty -> getResourceText(pathToBackingProperty)
    LazyDelegateProperty -> getResourceText(pathToLazyProperty)
}
