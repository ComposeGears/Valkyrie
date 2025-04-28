package io.github.composegears.valkyrie.internal

import libs
import org.gradle.api.Project

internal val Project.kotlinMultiplatformPluginId
    get() = libs.plugins.kotlin.multiplatform.get().pluginId

internal val Project.jetbrainsComposePluginId
    get() = libs.plugins.jetbrains.compose.get().pluginId

internal val Project.kotlinComposePluginId
    get() = libs.plugins.kotlin.compose.get().pluginId
