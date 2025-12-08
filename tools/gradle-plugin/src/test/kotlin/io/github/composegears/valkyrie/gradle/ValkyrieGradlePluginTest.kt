package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.common.doesNotExist
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.absolutePathString
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class ValkyrieGradlePluginTest : CommonGradleTest() {
    @TempDir
    lateinit var root: Path

    @BeforeEach
    fun beforeEach() {
        root.writeSettingsFile()
    }

    @Test
    fun `Package name doesn't need to be explicitly set if AGP is applied and namespace is set`() {
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

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 4 ImageVector icons in package \"a.b.c\"")
    }

    @Test
    fun `Setting custom package name`() {
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

        val result = buildRunner(root)
            .withArguments(TASK_NAME, "--info") // for log statement
            .build()

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 4 ImageVector icons in package \"my.custom.package\"")
    }

    @Test
    fun `Generate from SVGs with default config`() {
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

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")

        listOf(
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        ).forEach { filename ->
            assertThat(root.resolveGeneratedPath("main", "x/y/z/$filename")).exists()
        }
    }

    @OptIn(ExperimentalPathApi::class)
    @Test
    fun `Generate from test SVGs with custom config`() {
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

                    codeStyle {
                        useExplicitMode = true
                        indentSize = 8
                    }

                    imageVector {
                        outputFormat = OutputFormat.LazyProperty
                        useComposeColors = false
                        generatePreview = true
                        previewAnnotationType = PreviewAnnotationType.Jetbrains
                        addTrailingComma = true
                    }
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        listOf(
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        ).forEach { filename ->
            assertThat(root.resolveGeneratedPath("main", "x/y/z/$filename")).exists()
        }

        // and the LinearGradient file is created with the right visibility, parent pack, nested pack, etc
        val linearGradientKt = root
            .walk()
            .first { it.name == "LinearGradient.kt" }
            .readText()
        assertThat(linearGradientKt).contains("public val LinearGradient: ImageVector")
    }

    @Test
    fun `Generate from SVGs in KMP project`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 36

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

        val result = runTask(root, TASK_NAME)

        // no SVGs/drawables under these source sets, so the tasks are skipped (but still registered)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorAndroidFree", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorAndroidPaidDebug", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)

        // but androidMain and jvmMain have SVGs, so they run successfully
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorAndroidMain")
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorJvmMain")
    }

    @Test
    fun `Generate from drawables with default config`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 36
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "main")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        listOf(
            "OnlyPath.kt",
            "IconWithShorthandColor.kt",
            "SeveralPath.kt",
            "AllPathParams.kt",
        ).forEach { filename ->
            assertThat(root.resolveGeneratedPath("main", "x/y/z/$filename")).exists()
        }
    }

    @Test
    fun `Running the same task twice with configuration cache skips the second run`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 36
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
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("android")
                    id("com.android.library")
                    id("io.github.composegears.valkyrie")
                }

                android {
                    namespace = "x.y.z"
                    compileSdk = 36

                    flavorDimensions += "test"
                    productFlavors {
                        create("free") { dimension = "test" }
                        create("paid") { dimension = "test" }
                    }
                }

                dependencies {
                    implementation("$COMPOSE_UI")
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "debug") // just build variant
        root.writeTestSvgs(sourceSet = "freeRelease") // flavor + variant

        val result = runTask(root, TASK_NAME)

        // Then the specific variant tasks ran successfully
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorDebug")
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorFreeRelease")

        // and the wrapper
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVector")

        // and files were generated in the right source sets
        listOf(
            "build/generated/sources/valkyrie/freeRelease/kotlin/x/y/z/LinearGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/kotlin/x/y/z/RadialGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/kotlin/x/y/z/ClipPathGradient.kt",
            "build/generated/sources/valkyrie/freeRelease/kotlin/x/y/z/LinearGradientWithStroke.kt",
            "build/generated/sources/valkyrie/debug/kotlin/x/y/z/OnlyPath.kt",
            "build/generated/sources/valkyrie/debug/kotlin/x/y/z/IconWithShorthandColor.kt",
            "build/generated/sources/valkyrie/debug/kotlin/x/y/z/SeveralPath.kt",
            "build/generated/sources/valkyrie/debug/kotlin/x/y/z/AllPathParams.kt",
        ).forEach { path ->
            assertThat(root.resolve(path)).exists()
        }

        // but the empty source sets didn't generate anything
        listOf(
            "build/generated/sources/valkyrie/free/",
            "build/generated/sources/valkyrie/freeDebug/",
            "build/generated/sources/valkyrie/main/",
            "build/generated/sources/valkyrie/paid/",
            "build/generated/sources/valkyrie/paidDebug/",
            "build/generated/sources/valkyrie/paidRelease/",
            "build/generated/sources/valkyrie/release/",
            "build/generated/sources/valkyrie/test/",
        ).forEach { path ->
            assertThat(root.resolve(path)).doesNotExist()
        }
    }

    @Test
    fun `Compile JVM project generating and using ImageVectors`() {
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
                    implementation("$COMPOSE_UI")
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

        val result = runTask(root, "assemble")

        // codegen was hooked into compilation
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorMain")
        listOf(
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        ).forEach { filename ->
            assertThat(root.resolveGeneratedPath("main", "com/example/app/$filename")).exists()
        }

        // and the compilation succeeded, so the accessor code has access to the generated code dirs
        assertThat(result).taskWasSuccessful(":assemble")
    }

    @Test
    fun `Compile Android project generating and using ImageVectors`() {
        // pre-check - we need the Android SDK to pass this test. Skip test if it can't be found
        val androidHome = androidHomeOrSkip()

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
                    implementation("$COMPOSE_UI")
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

        val result = buildRunner(root)
            .withEnvironment(mapOf("ANDROID_HOME" to androidHome.absolutePathString()))
            .runTask("assemble")
            .build()

        // codegen was hooked into compilation
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorFreeDebug")
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorPaid")

        // and the compilation succeeded, so the accessor code has access to the generated code dirs
        assertThat(result).taskWasSuccessful(":assemble")
    }

    @Test
    fun `Run generate when syncing Intellij IDE`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "my.custom.package"
                    generateAtSync = true
                }

                // dummy task to replicate IntelliJ
                tasks.register("prepareKotlinIdeaImport")
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when the IDE syncs
        val result = buildRunner(root)
            .withArguments("prepareKotlinIdeaImport", "-Didea.sync.active=true")
            .build()

        // then the generate task was run
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorMain")
    }

    @Test
    fun `Don't run generate when syncing Intellij IDE if config wasn't enabled`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "my.custom.package"
                }

                // dummy task to replicate IntelliJ
                tasks.register("prepareKotlinIdeaImport")
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main")

        // when the IDE syncs
        val result = buildRunner(root)
            .withArguments("prepareKotlinIdeaImport", "-Didea.sync.active=true")
            .build()

        // then the generate task wasn't run
        assertThat(result.tasks).doesNotContain(":generateValkyrieImageVectorMain")
    }

    @Test
    fun `Generate from custom resource directory name`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"
                    resourceDirectoryName = "my-icons"
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "main", resourceDirName = "my-icons")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        listOf(
            "LinearGradient.kt",
            "RadialGradient.kt",
            "ClipPathGradient.kt",
            "LinearGradientWithStroke.kt",
        ).forEach { filename ->
            assertThat(root.resolveGeneratedPath("main", "x/y/z/$filename")).exists()
        }
    }

    @Test
    fun `Generate from custom resource directory name in KMP project`() {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"
                    resourceDirectoryName = "icons"
                }

                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )
        root.writeTestSvgs(sourceSet = "jvmMain", resourceDirName = "icons")
        root.writeTestDrawables(sourceSet = "jvmMain", resourceDirName = "icons")

        val result = runTask(root, TASK_NAME)

        // then - both SVGs and drawables from the custom directory are processed
        assertThat(result).taskWasSuccessful(":generateValkyrieImageVectorJvmMain")
        assertThat(result.output).contains("Generated 21 ImageVector icons in package \"x.y.z\"")
    }
}
