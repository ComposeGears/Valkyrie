package io.github.composegears.valkyrie.internal

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.kmpExtension(action: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure<KotlinMultiplatformExtension>(action)

internal fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) =
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)

internal fun Project.kover(action: KoverProjectExtension.() -> Unit) =
    extensions.configure<KoverProjectExtension>(action)
