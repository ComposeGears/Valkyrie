package io.github.composegears.valkyrie.generator.jvm.imagevector.common

import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourceText
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat.LazyProperty

fun OutputFormat.toResourceText(
    pathToBackingProperty: String,
    pathToLazyProperty: String,
): String = when (this) {
    BackingProperty -> getResourceText(pathToBackingProperty)
    LazyProperty -> getResourceText(pathToLazyProperty)
}
