package io.github.composegears.valkyrie.extension

import org.gradle.api.artifacts.ModuleDependency
import org.gradle.kotlin.dsl.exclude

fun ModuleDependency.excludeCompose() {
    exclude(group = "org.jetbrains.compose")
    exclude(group = "org.jetbrains.compose.foundation")
    exclude(group = "org.jetbrains.compose.runtime")
    exclude(group = "org.jetbrains.compose.ui")
}