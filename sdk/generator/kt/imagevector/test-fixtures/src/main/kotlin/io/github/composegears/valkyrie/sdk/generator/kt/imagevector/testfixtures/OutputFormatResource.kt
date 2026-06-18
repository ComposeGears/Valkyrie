package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.testfixtures

import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat.BackingProperty
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.OutputFormat.LazyProperty
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourceText

public fun OutputFormat.toResourceText(
    pathToBackingProperty: String,
    pathToLazyProperty: String,
): String = when (this) {
    BackingProperty -> getResourceText(pathToBackingProperty)
    LazyProperty -> getResourceText(pathToLazyProperty)
}
