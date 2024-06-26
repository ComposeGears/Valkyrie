package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.parser.ParserConfig
import java.io.File

val DEFAULT_CONFIG = ParserConfig(
    packPackage = "io.github.composegears.valkyrie.icons",
    packName = "ValkyrieIcons",
    generatePreview = false
)

fun loadIcon(name: String) = File("src/test/resources/${name}")