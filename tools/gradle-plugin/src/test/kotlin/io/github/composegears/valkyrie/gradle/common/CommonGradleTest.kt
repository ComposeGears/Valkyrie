package io.github.composegears.valkyrie.gradle.common

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.github.composegears.valkyrie.gradle.ANDROID_HOME
import io.github.composegears.valkyrie.gradle.GRADLE_VERSION
import io.github.composegears.valkyrie.gradle.RESOURCES_DIR_SVG
import io.github.composegears.valkyrie.gradle.RESOURCES_DIR_XML
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_RESOURCE_DIRECTORY
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS

open class CommonGradleTest {

    protected fun Path.resolveGeneratedPath(sourceSet: String, packagePath: String): Path {
        return resolve("$GENERATED_SOURCES_DIR/$sourceSet/kotlin/$packagePath")
    }

    protected fun Path.allGeneratedFiles(): List<Path> = resolve(GENERATED_SOURCES_DIR)
        .walk()
        .filter { it.isRegularFile() }
        .toList()

    protected fun buildRunner(root: Path): GradleRunner = GradleRunner
        .create()
        .withPluginClasspath()
        .withGradleVersion(GRADLE_VERSION)
        .withProjectDir(root.toFile())

    protected fun runTask(root: Path, task: String): BuildResult = buildRunner(root).runTask(task).build()

    protected fun failTask(root: Path, task: String): BuildResult = buildRunner(root).runTask(task).buildAndFail()

    protected fun GradleRunner.runTask(task: String): GradleRunner = withArguments(
        task,
        "--configuration-cache",
        "--stacktrace",
        "-Pandroid.useAndroidX=true", // needed for android builds to work, unused otherwise
    )

    protected fun Path.writeSettingsFile() = resolve("settings.gradle.kts").writeText(
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

    @OptIn(ExperimentalPathApi::class)
    protected fun Path.writeTestSvgs(
        sourceSet: String,
        resourceDirName: String = DEFAULT_RESOURCE_DIRECTORY,
    ) {
        val destDir = resolve("src/$sourceSet/$resourceDirName")
        destDir.createDirectories()

        val sourceDir = RESOURCES_DIR_SVG.toPath()
        sourceDir.copyToRecursively(destDir, followLinks = true)
    }

    @OptIn(ExperimentalPathApi::class)
    protected fun Path.writeTestDrawables(
        sourceSet: String,
        resourceDirName: String = DEFAULT_RESOURCE_DIRECTORY,
    ) {
        val destDir = resolve("src/$sourceSet/$resourceDirName")
        destDir.createDirectories()

        val sourceDir = RESOURCES_DIR_XML.toPath()
        sourceDir.copyToRecursively(destDir, followLinks = true)
    }

    protected fun Assert<BuildResult>.taskWasSuccessful(name: String) = taskHadResult(name, SUCCESS)

    protected fun Assert<BuildResult>.taskHadResult(
        path: String,
        expected: TaskOutcome,
    ) = transform { it.task(path)?.outcome }
        .isNotNull()
        .isEqualTo(expected)

    protected fun androidHomeOrSkip(): Path {
        val androidHome = ANDROID_HOME
        check(!androidHome.isNullOrBlank()) {
            "No Android home directory configured - skipping test"
        }

        val androidHomePath = Paths.get(androidHome)
        check(androidHomePath.exists()) {
            "Configured Android home directory ($androidHomePath) doesn't exist - skipping test"
        }

        return androidHomePath
    }
}
