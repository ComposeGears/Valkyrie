package io.github.composegears.valkyrie

import io.github.composegears.valkyrie.parser.Config
import java.io.File

val DEFAULT_CONFIG = Config(
    packName = "ValkyrieIcons",
    packPackage = "io.github.composegears.valkyrie.icons"
)

fun loadIcon(name: String) = File("src/test/resources/${name}")