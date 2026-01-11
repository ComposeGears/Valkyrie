package io.github.composegears.valkyrie.extension

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.exclude

fun ModuleDependency.excludeCompose() {
    exclude("org.jetbrains.compose")
    exclude("org.jetbrains.compose.foundation")
    exclude("org.jetbrains.compose.runtime")
    exclude("org.jetbrains.compose.ui")
}