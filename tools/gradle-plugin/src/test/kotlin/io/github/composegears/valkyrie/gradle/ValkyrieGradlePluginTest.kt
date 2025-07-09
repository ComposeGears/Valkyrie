package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import io.github.composegears.valkyrie.gradle.GenerateImageVectorsTask.Companion.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
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
                    compileSdk = 36
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when
        val result = runTask(root, TASK_NAME).build()

        // then
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
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
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
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
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
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
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
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

                    flavorDimensions += "test"
                    productFlavors {
                        create("free") { dimension = "test" }
                        create("paid") { dimension = "test" }
                    }
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

        // no files under the commonMain source set, so no task was run for it
        assertThat(result.tasks).doesNotContain(":generateImageVectorsCommonMain")

        // but androidMain and jvmMain were
        assertThat(result).taskWasSuccessful(":generateImageVectorsAndroidMain")
        assertThat(result).taskWasSuccessful(":generateImageVectorsJvmMain")
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
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains(
            "Generated 17 ImageVectors in package x.y.z",
            "OnlyPath.kt",
            "IconWithShorthandColor.kt",
            "SeveralPath.kt",
            "AllPathParams.kt",
            // plus loads of others
        )
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
        assertThat(result1).taskWasSuccessful(":$TASK_NAME")

        // Second run doesn't need to - no inputs have changed
        val result2 = runner.withArguments(TASK_NAME, "--configuration-cache").build()
        assertThat(result2).taskHadResult(":$TASK_NAME", UP_TO_DATE)
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

                dependencies {
                    implementation("${composeUi()}")
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "debug") // just build variant
        root.writeTestSvgs(sourceSet = "freeRelease") // flavor + variant

        val result = runTask(root, TASK_NAME).build()

        // Then the specific variant tasks ran successfully
        assertThat(result).taskWasSuccessful(":generateImageVectorsDebug")
        assertThat(result).taskWasSuccessful(":generateImageVectorsFreeRelease")

        // and the wrapper
        assertThat(result).taskWasSuccessful(":generateImageVectors")

        // and files were generated in the right source sets
        assertThat(result.output).contains(
            "build/generated/sources/valkyrie/freeRelease/LinearGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/RadialGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/ClipPathGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/LinearGradientWithStroke.kt",
            "build/generated/sources/valkyrie/debug/OnlyPath.kt",
            "build/generated/sources/valkyrie/debug/IconWithShorthandColor.kt",
            "build/generated/sources/valkyrie/debug/SeveralPath.kt",
            "build/generated/sources/valkyrie/debug/AllPathParams.kt",
        )

        // but the empty source sets didn't generate anything
        assertThat(result.output).doesNotContain(
            "build/generated/sources/valkyrie/free/",
            "build/generated/sources/valkyrie/freeDebug/",
            "build/generated/sources/valkyrie/main/",
            "build/generated/sources/valkyrie/paid/",
            "build/generated/sources/valkyrie/paidDebug/",
            "build/generated/sources/valkyrie/paidRelease/",
            "build/generated/sources/valkyrie/release/",
            "build/generated/sources/valkyrie/test/",
        )
    }

    @Test
    fun `Compile JVM project generating and using ImageVectors`() {
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

                dependencies {
                    implementation("${composeUi()}")
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

        val result = runTask(root, "assemble").build()

        // codegen was hooked into compilation
        assertThat(result).taskWasSuccessful(":generateImageVectorsMain")
        assertThat(result.output).contains(
            "Generated 4 ImageVectors in package com.example.app",
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        )

        // and the compilation succeeded, so the accessor code has access to the generated code dirs
        assertThat(result).taskWasSuccessful(":assemble")
    }

    @Test
    fun `Compile Android project generating and using ImageVectors`() {
        // pre-check - we need the Android SDK to pass this test. Skip test if it can't be found
        val androidHome = androidHomeOrSkip()

        // given
        root.writeTestSvgs(sourceSet = "freeDebug")
        root.writeTestDrawables(sourceSet = "paid")
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "com.example.app"
                    compileSdk = 36

                    flavorDimensions += "myFlavor"
                    productFlavors {
                        create("free") { dimension = "myFlavor" }
                        create("paid") { dimension = "myFlavor" }
                    }
                }

                kotlin {
                    // to match that used in validation.yml for CI
                    jvmToolchain(21)
                }

                dependencies {
                    implementation("${composeUi()}")
                }
            """.trimIndent(),
        )

        root.resolve("src/freeDebug/kotlin/").apply {
            createDirectories()
            resolve("FreeDebugTest.kt").writeText(
                """
                import com.example.app.LinearGradient

                fun accessFreeDebugSvgs() {
                    val linearGradient = LinearGradient
                }
                """.trimIndent(),
            )
        }

        root.resolve("src/paid/kotlin/").apply {
            createDirectories()
            resolve("PaidTest.kt").writeText(
                """
                import com.example.app.SeveralPath

                fun accessPaidDrawables() {
                    val severalPath = SeveralPath
                }
                """.trimIndent(),
            )
        }

        val result = buildRunner(root, androidHome)
            .runTask("assemble")
            .build()

        // codegen was hooked into compilation
        assertThat(result).taskWasSuccessful(":generateImageVectorsFreeDebug")
        assertThat(result).taskWasSuccessful(":generateImageVectorsPaid")

        // and the compilation succeeded, so the accessor code has access to the generated code dirs
        assertThat(result).taskWasSuccessful(":assemble")
    }
}
