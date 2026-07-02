package io.github.composegears.valkyrie.extension

import org.gradle.api.artifacts.Dependency

infix fun Dependency?.because(reason: String) {
    this?.because(reason)
}