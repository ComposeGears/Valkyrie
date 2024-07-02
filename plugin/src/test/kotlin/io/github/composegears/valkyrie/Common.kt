package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.processing.generator.imagevector.ImageVectorGeneratorConfig
import java.io.File

val DEFAULT_CONFIG = ImageVectorGeneratorConfig(
    packageName = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    nestedPackName = "",
    generatePreview = false
)

fun loadIcon(name: String) = File("src/test/resources/${name}")