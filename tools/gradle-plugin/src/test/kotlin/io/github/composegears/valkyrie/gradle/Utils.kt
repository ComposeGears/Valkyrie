@file:OptIn(ExperimentalPathApi::class)

package io.github.composegears.valkyrie.gradle

import assertk.Assert
import assertk.assertions.support.fail
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.writeText
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.Assumptions.assumeFalse
import org.junit.jupiter.api.Assumptions.assumeTrue

internal fun buildRunner(root: Path, androidHome: Path? = null) = GradleRunner
    .create()
    .withPluginClasspath()
    .withGradleVersion(System.getProperty("test.version.gradle"))
    .withProjectDir(root.toFile())
    .apply {
        if (androidHome != null) {
            withEnvironment(mapOf("ANDROID_HOME" to androidHome.absolutePathString()))
        }
    }

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

internal fun Assert<BuildResult>.taskHadResult(name: String, expected: TaskOutcome) = transform { result ->
    val outcome = result.task(name)?.outcome
    if (outcome == expected) return@transform
    fail(expected, outcome)
}

internal fun androidHomeOrSkip(): Path {
    val androidHome = System.getProperty("test.androidHome")
    assumeFalse(androidHome.isNullOrBlank())

    val androidHomePath = Paths.get(androidHome)
    assumeTrue(androidHomePath.exists())

    return androidHomePath
}

internal fun composeUi(): String = System.getProperty("test.composeUi")
