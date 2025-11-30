package io.github.composegears.valkyrie.gradle.common

import assertk.Assert
import assertk.fail
import java.nio.file.Files
import java.nio.file.Path

// TODO: https://github.com/assertk-org/assertk/pull/542
internal fun Assert<Path>.doesNotExist() = given { path ->
    if (Files.exists(path)) {
        fail("$path to not exist, but it does")
    }
}
