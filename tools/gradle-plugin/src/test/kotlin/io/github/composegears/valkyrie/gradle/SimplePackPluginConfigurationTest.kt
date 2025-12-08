package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.common.GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class SimplePackPluginConfigurationTest : CommonGradleTest() {

    @Test
    fun `generate only icon pack - commonMain`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("No icon files to process for ImageVector generation")

        val iconPackName = "ValkyrieIcons"
        assertThat(root.resolveGeneratedPath("commonMain", "x/y/z/$iconPackName.kt")).exists()

        assertThat(root.allGeneratedFiles().size).isEqualTo(1)
    }

    @Test
    fun `generate only icon pack - jvmMain`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "jvmMain"
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("No icon files to process for ImageVector generation")

        val iconPackName = "ValkyrieIcons"
        assertThat(root.resolveGeneratedPath("jvmMain", "x/y/z/$iconPackName.kt")).exists()

        assertThat(root.allGeneratedFiles().size).isEqualTo(1)
    }

    @Test
    fun `simple pack generation configuration from commonMain sourceSet`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonTest", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val iconPackName = "ValkyrieIcons"
        assertThat(root.resolveGeneratedPath("commonMain", "x/y/z/$iconPackName.kt")).exists()

        val generatedFiles = root.allGeneratedFiles()
        assertThat(generatedFiles.size).isEqualTo(18)

        generatedFiles
            .filter { it.fileName.toString() != "$iconPackName.kt" }
            .forEach { file ->
                val relativePath = root.resolve(GENERATED_SOURCES_DIR).relativize(file)
                assertThat(relativePath.toString()).contains("commonMain/kotlin/x/y/z/")

                assertThat(file.readText()).containsMatch("val $iconPackName.[A-Za-z0-9_]+: ImageVector".toRegex())
            }
    }
}
