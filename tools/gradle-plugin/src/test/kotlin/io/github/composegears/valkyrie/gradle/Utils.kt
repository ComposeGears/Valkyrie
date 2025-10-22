@file:OptIn(ExperimentalPathApi::class)

package io.github.composegears.valkyrie.gradle

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS

internal fun buildRunner(root: Path) = GradleRunner
    .create()
    .withPluginClasspath()
    .withGradleVersion(System.getProperty("test.version.gradle"))
    .withProjectDir(root.toFile())

internal fun runTask(root: Path, task: String) = buildRunner(root).runTask(task).build()

internal fun failTask(root: Path, task: String) = buildRunner(root).runTask(task).buildAndFail()

internal fun GradleRunner.runTask(task: String) = withArguments(
    task,
    "--configuration-cache",
    "--info",
    "--stacktrace",
    "-Pandroid.useAndroidX=true", // needed for android builds to work, unused otherwise
)

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

internal fun Assert<BuildResult>.taskWasSuccessful(name: String) = taskHadResult(name, SUCCESS)

internal fun Assert<BuildResult>.taskHadResult(path: String, expected: TaskOutcome) = transform { it.task(path)?.outcome }
    .isNotNull()
    .isEqualTo(expected)

internal fun androidHomeOrSkip(): Path {
    val androidHome = System.getProperty("test.androidHome")
    check(androidHome != null && androidHome.isNotBlank()) {
        "No Android home directory configured - skipping test"
    }

    val androidHomePath = Paths.get(androidHome)
    check(androidHomePath.exists()) {
        "Configured Android home directory ($androidHomePath) doesn't exist - skipping test"
    }

    return androidHomePath
}

internal fun composeUi(): String = System.getProperty("test.composeUi")
