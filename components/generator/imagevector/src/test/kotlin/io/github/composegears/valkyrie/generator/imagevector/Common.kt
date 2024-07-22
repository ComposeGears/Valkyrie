package io.github.composegears.valkyrie.generator.imagevector

import kotlin.io.path.Path

val DEFAULT_CONFIG = ImageVectorGeneratorConfig(
    packageName = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    nestedPackName = "",
    generatePreview = false
)

fun loadIcon(name: String) = Path("src/test/resources/${name}")