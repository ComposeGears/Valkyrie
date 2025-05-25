package io.github.composegears.valkyrie.gradle

import java.io.File
import org.gradle.testkit.runner.GradleRunner

internal fun runTask(root: File, task: String) = GradleRunner
    .create()
    .withDebug(true)
    .withPluginClasspath()
    .withGradleVersion(GRADLE_VERSION)
    .withProjectDir(root)
    .withArguments(task, "--info", "--stacktrace")

internal fun runTaskWithConfigurationCache(root: File, task: String) = GradleRunner
    .create()
    .withDebug(true)
    .withPluginClasspath()
    .withGradleVersion(GRADLE_VERSION)
    .withProjectDir(root)
    .withArguments(task, "--configuration-cache", "--info", "--stacktrace")

internal fun File.writeSettingsFile() = resolve("settings.gradle.kts").writeText(
    """
        pluginManagement {
            repositories {
                mavenCentral()
                google()
                gradlePluginPortal()
            }
        }

        dependencyResolutionManagement {
            repositories {
                mavenCentral()
                google()
            }
        }
    """.trimIndent(),
)

internal fun File.writeLibsTomlFile() = resolve("gradle")
    .also { it.mkdirs() }
    .resolve("libs.versions.toml")
    .writeText(
        """
            [plugins]
            agpLib = { id = "com.android.library", version = "$AGP_VERSION" }
            kotlinAndroid = { id = "org.jetbrains.kotlin.android", version = "$KOTLIN_VERSION" }
            kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version = "$KOTLIN_VERSION" }
            kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version = "$KOTLIN_VERSION" }
        """.trimIndent(),
    )
