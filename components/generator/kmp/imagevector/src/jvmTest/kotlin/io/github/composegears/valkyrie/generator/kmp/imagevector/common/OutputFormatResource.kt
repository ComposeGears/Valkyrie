package io.github.composegears.valkyrie.generator.kmp.imagevector.common

import io.github.composegears.valkyrie.generator.kmp.imagevector.OutputFormat
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText

internal fun OutputFormat.toResourceText(
    pathToBackingProperty: String,
    pathToLazyProperty: String,
): String = when (this) {
    OutputFormat.BackingProperty -> getResourceText(pathToBackingProperty)
    OutputFormat.LazyProperty -> getResourceText(pathToLazyProperty)
}
