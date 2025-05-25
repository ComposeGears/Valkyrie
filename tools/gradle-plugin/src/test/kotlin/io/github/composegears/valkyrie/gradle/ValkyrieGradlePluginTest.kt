package io.github.composegears.valkyrie.gradle

import io.github.composegears.valkyrie.gradle.GenerateSvgImageVectorTask.Companion.TASK_NAME
import java.io.File
import kotlin.test.assertContains
import kotlin.test.assertEquals
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ValkyrieGradlePluginTest {
    @TempDir lateinit var root: File

    private lateinit var project: DefaultProject

    @BeforeEach
    fun before() {
        root.writeLibsTomlFile()
        root.writeSettingsFile()
        project = ProjectBuilder
            .builder()
            .withProjectDir(root)
            .build()
            as DefaultProject
    }

    @Test
    fun `Package name doesn't need to be explicitly set if AGP is applied and namespace is set`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinAndroid)
                    alias(libs.plugins.agpLib)
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
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 0 ImageVectors in package a.b.c")
    }

    @Test
    fun `Package name does need to be explicitly set if AGP isn't applied`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinJvm)
                    id("io.github.composegears.valkyrie")
                }
            """.trimIndent(),
        )

        // when
        val result = runTask(root, TASK_NAME).buildAndFail()

        // then
        assertContains(result.output, "Could not create task ':$TASK_NAME'")
        assertContains(result.output, "Couldn't automatically estimate package name")
    }

    @Test
    fun `Setting custom package name`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinJvm)
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
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 0 ImageVectors in package x.y.z")
    }

    @Test
    fun `Generate from SVGs with default config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinJvm)
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
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 4 ImageVectors in package x.y.z")
        assertContains(result.output, "LinearGradient.kt")
        assertContains(result.output, "RadialGradient.kt")
        assertContains(result.output, "ClipPathGradient.kt")
        assertContains(result.output, "LinearGradientWithStroke.kt")
    }

    @Test
    fun `Generate from test SVGs with custom config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                import io.github.composegears.valkyrie.generator.jvm.imagevector.OutputFormat
                import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType

                plugins {
                    alias(libs.plugins.kotlinJvm)
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
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 4 ImageVectors in package x.y.z")
        assertContains(result.output, "LinearGradient.kt")
        assertContains(result.output, "RadialGradient.kt")
        assertContains(result.output, "ClipPathGradient.kt")
        assertContains(result.output, "LinearGradientWithStroke.kt")

        // and the LinearGradient file is created with the right visibility, parent pack, nested pack, etc
        val linearGradientKt = project
            .fileTree(root)
            .first { it.name == "LinearGradient.kt" }
            .readText()
        assertContains(linearGradientKt, "public val MyIconPack.MyNestedPack.LinearGradient: ImageVector")
    }

    @Test
    fun `Generate from SVGs in KMP project`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinMultiplatform)
                    alias(libs.plugins.agpLib)
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
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 4 ImageVectors in package x.y.z")
        assertContains(result.output, "LinearGradient.kt")
        assertContains(result.output, "RadialGradient.kt")
        assertContains(result.output, "ClipPathGradient.kt")
        assertContains(result.output, "LinearGradientWithStroke.kt")
    }

    @Test
    fun `Generate from drawables with default config`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinAndroid)
                    alias(libs.plugins.agpLib)
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
        val result = runTaskWithConfigurationCache(root, TASK_NAME).build()

        // then
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)
        assertContains(result.output, "Generated 13 ImageVectors in package x.y.z")
        assertContains(result.output, "OnlyPath.kt")
        assertContains(result.output, "IconWithShorthandColor.kt")
        assertContains(result.output, "SeveralPath.kt")
        assertContains(result.output, "AllPathParams.kt")
        // plus loads others
    }

    @Test
    fun `Running the same task twice with configuration cache skips the second run`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    alias(libs.plugins.kotlinAndroid)
                    alias(libs.plugins.agpLib)
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
        val result = runTaskWithConfigurationCache(root, TASK_NAME).build()
        assertEquals(TaskOutcome.SUCCESS, result.task(":$TASK_NAME")?.outcome)

        // Second run doesn't need to - no inputs have changed
        val result2 = runTaskWithConfigurationCache(root, TASK_NAME).build()
        assertEquals(TaskOutcome.UP_TO_DATE, result2.task(":$TASK_NAME")?.outcome)
    }

    private fun writeTestSvgs(sourceSet: String = "main") {
        val svgDir = root.resolve("src/$sourceSet/svg")
        svgDir.mkdirs()

        File(SAMPLE_SVG_DIR)
            .listFiles()
            .orEmpty()
            .forEach { file -> file.copyTo(svgDir.resolve(file.name)) }
    }

    private fun writeTestDrawables(sourceSet: String = "main") {
        val xmlDir = root.resolve("src/$sourceSet/res/drawable")
        xmlDir.mkdirs()

        File(SAMPLE_XML_DIR)
            .listFiles()
            .orEmpty()
            .forEach { file -> file.copyTo(xmlDir.resolve(file.name)) }
    }
}
