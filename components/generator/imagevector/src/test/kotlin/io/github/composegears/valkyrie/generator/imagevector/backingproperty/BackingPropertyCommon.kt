package io.github.composegears.valkyrie.generator.imagevector.backingproperty

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.BackingProperty
import kotlin.io.path.Path

val DEFAULT_BACKING_CONFIG = ImageVectorGeneratorConfig(
    packageName = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    nestedPackName = "",
    outputFormat = BackingProperty,
    generatePreview = false,
)

fun loadIcon(name: String) = Path("src/test/resources/$name")
