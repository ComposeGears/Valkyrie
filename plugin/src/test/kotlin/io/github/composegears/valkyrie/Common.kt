package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.processing.parser.ParserConfig
import java.io.File

val DEFAULT_CONFIG = ParserConfig(
    packageName = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    nestedPackName = "",
    generatePreview = false
)

fun loadIcon(name: String) = File("src/test/resources/${name}")