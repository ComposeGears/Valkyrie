package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.GenerateSvgImageVectorTask.Companion.TASK_NAME
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.name
import kotlin.io.path.writeText
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ValkyrieGradlePluginTest {
    @TempDir lateinit var root: Path

    private lateinit var project: DefaultProject

    @BeforeEach
    fun before() {
        root.writeSettingsFile()
        project = ProjectBuilder
            .builder()
            .withProjectDir(root.toFile())
            .build()
            as DefaultProject
    }

    @Test
    fun `Package name doesn't need to be explicitly set if AGP is applied and namespace is set`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "a.b.c"
                    compileSdk = 33
                }
            """.trimIndent(),
        )

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 0 ImageVectors in package a.b.c")
        assertThat(result.output).contains("Generated 0 ImageVectors in package a.b.c")
    }

    @Test
    fun `Package name does need to be explicitly set if AGP isn't applied`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }
            """.trimIndent(),
        )

        // when
        val result = runTask(root, TASK_NAME).buildAndFail()

        // then
        assertThat(result.output).contains("Could not create task ':$TASK_NAME'")
        assertThat(result.output).contains("Couldn't automatically estimate package name")
    }

    @Test
    fun `Setting custom package name`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"
                }
            """.trimIndent(),
        )

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 0 ImageVectors in package x.y.z")
    }

    @Test
    fun `Generate from SVGs with default config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"
                }
            """.trimIndent(),
        )

        writeTestSvgs()

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 4 ImageVectors in package x.y.z")
        assertThat(result.output).contains("LinearGradient.kt")
        assertThat(result.output).contains("RadialGradient.kt")
        assertThat(result.output).contains("ClipPathGradient.kt")
        assertThat(result.output).contains("LinearGradientWithStroke.kt")
    }

    @Test
    fun `Generate from test SVGs with custom config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
                import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType

                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"
                    configure {
                        iconPackName = "MyIconPack"
                        nestedPackName = "MyNestedPack"
                        outputFormat = OutputFormat.LazyProperty
                        useComposeColors = false
                        generatePreview = true
                        previewAnnotationType = PreviewAnnotationType.Jetbrains
                        useFlatPackage = true
                        useExplicitMode = true
                        addTrailingComma = true
                        indentSize = 8
                    }
                }
            """.trimIndent(),
        )

        writeTestSvgs()

        // when
        val result = runTask(root, TASK_NAME).build()

        // then the expected files are printed to log
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 4 ImageVectors in package x.y.z")
        assertThat(result.output).contains("LinearGradient.kt")
        assertThat(result.output).contains("RadialGradient.kt")
        assertThat(result.output).contains("ClipPathGradient.kt")
        assertThat(result.output).contains("LinearGradientWithStroke.kt")

        // and the LinearGradient file is created with the right visibility, parent pack, nested pack, etc
        val linearGradientKt = project
            .fileTree(root)
            .first { it.name == "LinearGradient.kt" }
            .readText()
        assertThat(linearGradientKt).contains("public val MyIconPack.MyNestedPack.LinearGradient: ImageVector")
    }

    @Test
    fun `Generate from SVGs in KMP project`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 35
                }

                kotlin {
                    androidTarget()
                    jvm()
                }
            """.trimIndent(),
        )

        writeTestSvgs(sourceSet = "commonMain")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 4 ImageVectors in package x.y.z")
        assertThat(result.output).contains("LinearGradient.kt")
        assertThat(result.output).contains("RadialGradient.kt")
        assertThat(result.output).contains("ClipPathGradient.kt")
        assertThat(result.output).contains("LinearGradientWithStroke.kt")
    }

    @Test
    fun `Generate from drawables with default config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 35
                }
            """.trimIndent(),
        )

        writeTestDrawables()

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)
        assertThat(result.output).contains("Generated 13 ImageVectors in package x.y.z")
        assertThat(result.output).contains("OnlyPath.kt")
        assertThat(result.output).contains("IconWithShorthandColor.kt")
        assertThat(result.output).contains("SeveralPath.kt")
        assertThat(result.output).contains("AllPathParams.kt")
        // plus loads others
    }

    @Test
    fun `Running the same task twice with configuration cache skips the second run`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 35
                }
            """.trimIndent(),
        )

        writeTestDrawables()
        writeTestSvgs()

        // First run generates the outputs
        val result = runTask(root, TASK_NAME).build()
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.SUCCESS)

        // Second run doesn't need to - no inputs have changed
        val result2 = runTask(root, TASK_NAME).build()
        assertThat(result2.task(":$TASK_NAME")?.outcome).isEqualTo(TaskOutcome.UP_TO_DATE)
    }

    private fun writeTestSvgs(sourceSet: String = "main") {
        val destDir = root.resolve("src/$sourceSet/svg")
        Files.createDirectories(destDir)

        val sourceDir = Paths.get(System.getProperty("test.dir.svg"))
        Files.list(sourceDir).forEach { p -> Files.copy(p, destDir.resolve(p.name)) }
    }

    private fun writeTestDrawables(sourceSet: String = "main") {
        val destDir = root.resolve("src/$sourceSet/res/drawable")
        Files.createDirectories(destDir)

        val sourceDir = Paths.get(System.getProperty("test.dir.xml"))
        Files.list(sourceDir).forEach { p -> Files.copy(p, destDir.resolve(p.name)) }
    }
}
