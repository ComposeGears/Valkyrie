package io.github.composegears.valkyrie.internal

import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationExtension
import org.jetbrains.kotlin.gradle.dsl.abi.AbiValidationMultiplatformExtension

internal fun Project.kmpExtension(action: KotlinMultiplatformExtension.() -> Unit) =
    extensions.configure<KotlinMultiplatformExtension>(action)

internal fun Project.kotlinJvm(action: KotlinJvmProjectExtension.() -> Unit) =
    extensions.configure<KotlinJvmProjectExtension>(action)

internal fun KotlinMultiplatformExtension.abiValidation(configure: AbiValidationMultiplatformExtension.() -> Unit): Unit =
    extensions.configure<AbiValidationMultiplatformExtension>(configure)

internal fun KotlinJvmProjectExtension.abiValidation(configure: AbiValidationExtension.() -> Unit): Unit =
    extensions.configure<AbiValidationExtension>(configure)

internal fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) =
    extensions.configure<ComposeCompilerGradlePluginExtension>(block)

internal fun Project.kover(action: KoverProjectExtension.() -> Unit) =
    extensions.configure<KoverProjectExtension>(action)

internal fun Project.configureArchiveBaseName() {
    tasks.withType<Jar>().configureEach {
        val pathSegments = project.path
            .removePrefix(":")
            .split(":")
            .filter { it.isNotEmpty() }
        val name = pathSegments.joinToString("-")

        archiveBaseName.set("valkyrie-$name")
    }
}
