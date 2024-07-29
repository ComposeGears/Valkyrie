package io.github.composegears.valkyrie.generator.imagevector.lazyproperty

import io.github.composegears.valkyrie.generator.imagevector.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat.LazyDelegateProperty

val DEFAULT_LAZY_CONFIG = ImageVectorGeneratorConfig(
    packageName = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    nestedPackName = "",
    outputFormat = LazyDelegateProperty,
    generatePreview = false,
)
