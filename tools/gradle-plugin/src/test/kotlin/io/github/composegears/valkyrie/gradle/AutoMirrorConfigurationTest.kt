package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.name
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class AutoMirrorConfigurationTest : CommonGradleTest() {

    @Test
    fun `autoMirror true at root level affects all icons`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                    autoMirror = true
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val generatedFiles = root.allGeneratedFiles()
        assertThat(generatedFiles.size).isEqualTo(17)

        // Verify all icons have autoMirror = true
        generatedFiles.forEach { file ->
            assertThat(file.readText()).contains("autoMirror = true")
        }
    }

    @Test
    fun `autoMirror false at root level affects all icons`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                    autoMirror = false
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val generatedFiles = root.allGeneratedFiles()
        assertThat(generatedFiles.size).isEqualTo(17)

        // Verify all icons don't have autoMirror
        generatedFiles.forEach { file ->
            assertThat(file.readText()).doesNotContain("autoMirror")
        }
    }

    @Test
    fun `autoMirror at iconPack level overrides root level`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                    autoMirror = false

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"
                        autoMirror = true
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val iconPackName = "ValkyrieIcons"
        assertThat(root.resolveGeneratedPath("commonMain", "x/y/z/$iconPackName.kt")).exists()

        val generatedFiles = root.allGeneratedFiles()
        assertThat(generatedFiles.size).isEqualTo(18)

        // Verify all icons have autoMirror = true (iconPack level overrides root)
        generatedFiles
            .filter { it.fileName.toString() != "$iconPackName.kt" }
            .forEach { file ->
                assertThat(file.readText()).contains("autoMirror = true")
            }
    }

    @Test
    fun `autoMirror at nested pack level overrides iconPack level`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                    autoMirror = false

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"
                        autoMirror = true

                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                            autoMirror = false
                        }

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeNestedPackDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")

        val iconPackName = "ValkyrieIcons"
        assertThat(root.resolveGeneratedPath("commonMain", "x/y/z/$iconPackName.kt")).exists()

        // Check Outlined nested pack icons have autoMirror = false
        val outlinedFiles = root.resolveGeneratedPath("commonMain", "x/y/z/outlined").walk().toList()
        outlinedFiles.forEach { file ->
            assertThat(file.readText()).doesNotContain("autoMirror")
        }

        // Check Filled nested pack icons have autoMirror = true (from iconPack level)
        val filledFiles = root.resolveGeneratedPath("commonMain", "x/y/z/filled").walk().toList()
        filledFiles.forEach { file ->
            val content = file.readText()
            assertThat(content).contains("autoMirror = true")
        }
    }

    @Test
    fun `no autoMirror configuration preserves original values`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeTestDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val generatedFiles = root.allGeneratedFiles()
        assertThat(generatedFiles.size).isEqualTo(17)

        // All test drawables shouldn't have autoMirror (from the source files)
        generatedFiles.forEach { file ->
            if (file.name == "AllPathParams.kt") {
                assertThat(file.readText()).contains("autoMirror = true")
            } else {
                assertThat(file.readText()).doesNotContain("autoMirror")
            }
        }
    }

    @Test
    fun `autoMirror in nested pack without iconPack level configuration`(@TempDir root: Path) {
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

                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                            autoMirror = true
                        }

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                            autoMirror = false
                        }
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeNestedPackDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")

        // Check Outlined nested pack icons have autoMirror = true
        val outlinedFiles = root.resolveGeneratedPath("commonMain", "x/y/z/outlined").walk().toList()
        outlinedFiles.forEach { file ->
            assertThat(file.readText()).contains("autoMirror = true")
        }

        // Check Filled nested pack icons don't have autoMirror
        val filledFiles = root.resolveGeneratedPath("commonMain", "x/y/z/filled").walk().toList()
        filledFiles.forEach { file ->
            assertThat(file.readText()).doesNotContain("autoMirror")
        }
    }

    @Test
    fun `autoMirror at root level with nested pack`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"
                    autoMirror = true

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"

                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                    }
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        root.writeNestedPackDrawables(sourceSet = "commonMain")

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")

        // Check Outlined nested pack icons have autoMirror = true
        val outlinedFiles = root.resolveGeneratedPath("commonMain", "x/y/z/outlined").walk().toList()
        outlinedFiles.forEach { file ->
            assertThat(file.readText()).contains("autoMirror = true")
        }

        // Check Filled nested pack icons have autoMirror = true
        val filledFiles = root.resolveGeneratedPath("commonMain", "x/y/z/filled").walk().toList()
        filledFiles.forEach { file ->
            assertThat(file.readText()).contains("autoMirror = true")
        }
    }

    private fun Path.writeNestedPackDrawables(sourceSet: String) {
        val resourceDir = resolve("src/$sourceSet/valkyrieResources")
        val outlinedDir = resourceDir.resolve("outlined")
        val filledDir = resourceDir.resolve("filled")

        outlinedDir.createDirectories()
        filledDir.createDirectories()

        // Write some test XML drawables for outlined
        outlinedDir.resolve("outlined_icon1.xml").writeText(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
                <path
                    android:fillColor="#FF000000"
                    android:pathData="M12,2L2,7v10c0,5.55 3.84,10.74 9,12 5.16,-1.26 9,-6.45 9,-12L20,7l-10,-5z"/>
            </vector>
            """.trimIndent(),
        )

        outlinedDir.resolve("outlined_icon2.xml").writeText(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
                <path
                    android:fillColor="#FF000000"
                    android:pathData="M12,2C6.48,2 2,6.48 2,12s4.48,10 10,10 10,-4.48 10,-10S17.52,2 12,2z"/>
            </vector>
            """.trimIndent(),
        )

        // Write some test XML drawables for filled
        filledDir.resolve("filled_icon1.xml").writeText(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
                <path
                    android:fillColor="#FF000000"
                    android:pathData="M19,3L5,3c-1.1,0 -2,0.9 -2,2v14c0,1.1 0.9,2 2,2h14c1.1,0 2,-0.9 2,-2L21,5c0,-1.1 -0.9,-2 -2,-2z"/>
            </vector>
            """.trimIndent(),
        )

        filledDir.resolve("filled_icon2.xml").writeText(
            """
            <vector xmlns:android="http://schemas.android.com/apk/res/android"
                android:width="24dp"
                android:height="24dp"
                android:viewportWidth="24"
                android:viewportHeight="24">
                <path
                    android:fillColor="#FF000000"
                    android:pathData="M12,17.27L18.18,21l-1.64,-7.03L22,9.24l-7.19,-0.61L12,2 9.19,8.63 2,9.24l5.46,4.73L5.82,21z"/>
            </vector>
            """.trimIndent(),
        )
    }
}
