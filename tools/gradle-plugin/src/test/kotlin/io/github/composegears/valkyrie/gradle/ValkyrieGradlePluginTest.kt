package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.GenerateSvgImageVectorTask.Companion.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ValkyrieGradlePluginTest {
    @TempDir lateinit var root: Path

    @BeforeEach
    fun before() {
        root.writeSettingsFile()
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
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.output).contains("Generated 4 ImageVectors in package a.b.c")
    }

    @Test
    fun `Package name needs to be explicitly set if AGP isn't applied`() {
        // given
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).buildAndFail()

        // then
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
                    packageName = "my.custom.package"
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.output).contains("Generated 4 ImageVectors in package my.custom.package")
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
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.output).contains(
            "Generated 4 ImageVectors in package x.y.z",
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        )
    }

    @OptIn(ExperimentalPathApi::class)
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
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then the expected files are printed to log
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.output).contains(
            "Generated 4 ImageVectors in package x.y.z",
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        )

        // and the LinearGradient file is created with the right visibility, parent pack, nested pack, etc
        val linearGradientKt = root
            .walk()
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

        root.writeTestSvgs(sourceSet = "jvmMain")
        root.writeTestSvgs(sourceSet = "androidMain")

        // when
        val result = runTask(root, TASK_NAME).build()

        // no files under the commonMain source set, so no task was run
        assertThat(result.tasks).doesNotContain(":generateImageVectorCommonMain")

        // but androidMain and jvmMain were
        assertThat(result.task(":generateImageVectorAndroidMain")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.task(":generateImageVectorJvmMain")?.outcome).isEqualTo(SUCCESS)
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

        root.writeTestDrawables(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.output).contains("Generated 14 ImageVectors in package x.y.z")
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

        root.writeTestDrawables(sourceSet = "main")
        root.writeTestSvgs(sourceSet = "main")

        // First run generates the outputs
        val runner = buildRunner(root)
        val result1 = runner.withArguments(TASK_NAME, "--configuration-cache").build()
        assertThat(result1.task(":$TASK_NAME")?.outcome).isEqualTo(SUCCESS)

        // Second run doesn't need to - no inputs have changed
        val result2 = runner.withArguments(TASK_NAME, "--configuration-cache").build()
        assertThat(result2.task(":$TASK_NAME")?.outcome).isEqualTo(UP_TO_DATE)
    }

    @Test
    fun `Generate outputs for android build variants`() {
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

                    flavorDimensions += "test"
                    productFlavors {
                        create("free") { dimension = "test" }
                        create("paid") { dimension = "test" }
                    }
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "debug") // just build variant
        root.writeTestSvgs(sourceSet = "freeRelease") // flavor + variant

        // Then both tasks ran successfully
        val result = runTask(root, TASK_NAME).build()
        assertThat(result.task(":generateImageVectorDebug")?.outcome).isEqualTo(SUCCESS)
        assertThat(result.task(":generateImageVectorFreeRelease")?.outcome).isEqualTo(SUCCESS)
    }

    @Test
    fun `Compile project generating and using ImageVectors`() {
        // given
        root.writeTestSvgs(sourceSet = "main")
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "com.example.app"
                }
            """.trimIndent(),
        )

        val sourceDir = root.resolve("src/main/kotlin/")
        sourceDir.createDirectories()
        sourceDir.resolve("Test.kt").writeText(
            """
                import com.example.app.LinearGradient

                fun accessGeneratedData() {
                    val linearGradient = LinearGradient
                }
            """.trimIndent(),
        )

        val result = runTask(root, "build").build()
        assertThat(result.task(":build")?.outcome).isEqualTo(SUCCESS)
    }
}
