@file:OptIn(ExperimentalPathApi::class)

package io.github.composegears.valkyrie.gradle

import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.gradle.testkit.runner.GradleRunner

internal fun buildRunner(root: Path) = GradleRunner
    .create()
    .withPluginClasspath()
    .withGradleVersion(System.getProperty("test.version.gradle"))
    .withProjectDir(root.toFile())

internal fun runTask(root: Path, task: String) = buildRunner(root)
    .withArguments(task, "--configuration-cache", "--info", "--stacktrace")

internal fun Path.writeSettingsFile() = resolve("settings.gradle.kts").writeText(
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

internal fun Path.writeTestSvgs(sourceSet: String) {
    val destDir = resolve("src/$sourceSet/svg")
    destDir.createDirectories()

    val sourceDir = Paths.get(System.getProperty("test.dir.svg"))
    sourceDir.copyToRecursively(destDir, followLinks = true)
}

internal fun Path.writeTestDrawables(sourceSet: String) {
    val destDir = resolve("src/$sourceSet/res/drawable")
    destDir.createDirectories()

    val sourceDir = Paths.get(System.getProperty("test.dir.xml"))
    sourceDir.copyToRecursively(destDir, followLinks = true)
}
