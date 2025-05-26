package io.github.composegears.valkyrie.gradle

import java.io.File
import org.gradle.testkit.runner.GradleRunner

internal fun runTask(root: File, task: String) = GradleRunner
    .create()
    .withPluginClasspath()
    .withGradleVersion(System.getProperty("test.version.gradle"))
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

internal fun File.writeLibsTomlFile() {
    val agp = System.getProperty("test.version.agp")
    val kotlin = System.getProperty("test.version.kotlin")
    resolve("gradle")
        .also { it.mkdirs() }
        .resolve("libs.versions.toml")
        .writeText(
            """
                [plugins]
                agpLib = { id = "com.android.library", version = "$agp" }
                kotlinAndroid = { id = "org.jetbrains.kotlin.android", version = "$kotlin" }
                kotlinJvm = { id = "org.jetbrains.kotlin.jvm", version = "$kotlin" }
                kotlinMultiplatform = { id = "org.jetbrains.kotlin.multiplatform", version = "$kotlin" }
            """.trimIndent(),
        )
}
