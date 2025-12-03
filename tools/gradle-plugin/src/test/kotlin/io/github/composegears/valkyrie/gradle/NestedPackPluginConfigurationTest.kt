package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.common.GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.common.doesNotExist
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.gradle.testkit.runner.TaskOutcome.UP_TO_DATE
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class NestedPackPluginConfigurationTest : CommonGradleTest() {

    @Test
    fun `generate icon pack with custom style`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "x.y.z"

                    codeStyle {
                        useExplicitMode = true
                        indentSize = 2
                    }

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

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVector", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("No icon files to process for ImageVector generation")

        val expectedCode = """
            package x.y.z

            public object ValkyrieIcons {
              public object Outlined

              public object Filled
            }

        """.trimIndent()
        val generatedFile = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/x/y/z/ValkyrieIcons.kt")
        assertThat(generatedFile).exists()
        assertThat(generatedFile.readText()).isEqualTo(expectedCode)

        val generatedFiles = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedFiles.count()).isEqualTo(1)
    }

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

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVector", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("No icon files to process for ImageVector generation")

        val expectedCode = """
            package x.y.z

            object ValkyrieIcons {
                object Outlined

                object Filled
            }

        """.trimIndent()
        val generatedFile = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/x/y/z/ValkyrieIcons.kt")
        assertThat(generatedFile).exists()
        assertThat(generatedFile.readText()).isEqualTo(expectedCode)

        val generatedFiles = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedFiles.count()).isEqualTo(1)

        // Run task again to verify UP-TO-DATE behavior
        val result2 = runTask(root, TASK_NAME)
        assertThat(result2).taskHadResult(":generateValkyrieImageVectorCommonMain", UP_TO_DATE)
        assertThat(result2).taskHadResult(":generateValkyrieImageVector", UP_TO_DATE)
        assertThat(result2).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result2.output).doesNotContain("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result2.output).doesNotContain("No icon files to process for ImageVector generation")

        val generatedFile2 = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/x/y/z/ValkyrieIcons.kt")
        assertThat(generatedFile2).exists()
        assertThat(generatedFile2.readText()).isEqualTo(expectedCode)
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

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVector", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result.output).contains("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result.output).contains("No icon files to process for ImageVector generation")

        val expectedCode = """
            package x.y.z

            object ValkyrieIcons {
                object Outlined

                object Filled
            }

        """.trimIndent()
        val generatedFile = root.resolve("${GENERATED_SOURCES_DIR}/jvmMain/x/y/z/ValkyrieIcons.kt")
        assertThat(generatedFile).exists()
        assertThat(generatedFile.readText()).isEqualTo(expectedCode)

        val generatedFiles = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedFiles.count()).isEqualTo(1)

        // Run task again to verify UP-TO-DATE behavior
        val result2 = runTask(root, TASK_NAME)
        assertThat(result2).taskHadResult(":generateValkyrieImageVectorCommonMain", SKIPPED)
        assertThat(result2).taskHadResult(":generateValkyrieImageVector", UP_TO_DATE)
        assertThat(result2).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result2.output).doesNotContain("Generated \"ValkyrieIcons\" iconpack in package \"x.y.z\"")
        assertThat(result2.output).doesNotContain("No icon files to process for ImageVector generation")

        val generatedFile2 = root.resolve("${GENERATED_SOURCES_DIR}/jvmMain/x/y/z/ValkyrieIcons.kt")
        assertThat(generatedFile2).exists()
        assertThat(generatedFile2.readText()).isEqualTo(expectedCode)
    }

    @Test
    fun `generate icons from nested pack source folders`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "com.example.icons"

                    iconPack {
                        name = "MyIcons"
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

        // Create nested directories with SVG files
        val sourceDir = RESOURCES_DIR_SVG.toPath()
        val resourceDir = root.resolve("src/commonMain/valkyrieResources")

        val outlinedDir = resourceDir.resolve("outlined").createDirectories()
        sourceDir.resolve("ic_clip_path_gradient.svg").copyTo(outlinedDir.resolve("ic_clip_path_gradient.svg"))
        sourceDir.resolve("ic_linear_gradient_with_stroke.svg")
            .copyTo(outlinedDir.resolve("ic_linear_gradient_with_stroke.svg"))

        val filledDir = resourceDir.resolve("filled").createDirectories()
        sourceDir.resolve("ic_linear_gradient.svg").copyTo(filledDir.resolve("ic_linear_gradient.svg"))
        sourceDir.resolve("ic_radial_gradient.svg").copyTo(filledDir.resolve("ic_radial_gradient.svg"))

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVector", SUCCESS)
        assertThat(result.output).contains("Generated \"MyIcons\" iconpack in package \"com.example.icons\"")
        assertThat(result.output).contains("Generated 2 ImageVector icons in nested pack \"Outlined\" (package: \"com.example.icons.outlined\")")
        assertThat(result.output).contains("Generated 2 ImageVector icons in nested pack \"Filled\" (package: \"com.example.icons.filled\")")

        val expectedCode = """
            package com.example.icons

            object MyIcons {
                object Outlined

                object Filled
            }

        """.trimIndent()

        val iconPackFile = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/MyIcons.kt")
        assertThat(iconPackFile).exists()
        assertThat(iconPackFile.readText()).isEqualTo(expectedCode)

        // Count total generated files (1 pack file + 4 icon files)
        val generatedFiles = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedFiles.size).isEqualTo(5)

        // Verify outlined icons are in the correct package
        val outlinedFiles = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/outlined")
        val outlinedIconFiles = outlinedFiles.toFile().listFiles().orEmpty()
        assertThat(outlinedIconFiles.size).isEqualTo(2)

        outlinedIconFiles.forEach { file ->
            val content = file.readText()
            assertThat(content).contains("package com.example.icons.outlined")
            assertThat(content).contains("val MyIcons.Outlined.")
        }

        // Verify filled icons are in the correct package
        val filledFiles = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/filled")
        val filledIconFiles = filledFiles.toFile().listFiles().orEmpty()
        assertThat(filledIconFiles.size).isEqualTo(2)

        filledIconFiles.forEach { file ->
            val content = file.readText()
            assertThat(content).contains("package com.example.icons.filled")
            assertThat(content).contains("val MyIcons.Filled.")
        }
    }

    @Test
    fun `generate icons from nested pack source folders with useFlatPackage`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "com.example.icons"

                    iconPack {
                        name = "MyIcons"
                        targetSourceSet = "commonMain"
                        useFlatPackage = true

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

        val sourceDir = RESOURCES_DIR_SVG.toPath()
        val resourceDir = root.resolve("src/commonMain/valkyrieResources")

        val outlinedDir = resourceDir.resolve("outlined").createDirectories()
        sourceDir.resolve("ic_clip_path_gradient.svg").copyTo(outlinedDir.resolve("ic_clip_path_gradient.svg"))
        sourceDir.resolve("ic_linear_gradient_with_stroke.svg")
            .copyTo(outlinedDir.resolve("ic_linear_gradient_with_stroke.svg"))

        val filledDir = resourceDir.resolve("filled").createDirectories()
        sourceDir.resolve("ic_linear_gradient.svg").copyTo(filledDir.resolve("ic_linear_gradient.svg"))
        sourceDir.resolve("ic_radial_gradient.svg").copyTo(filledDir.resolve("ic_radial_gradient.svg"))

        val result = runTask(root, TASK_NAME)

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonMain", SUCCESS)
        assertThat(result).taskHadResult(":generateValkyrieImageVector", SUCCESS)
        assertThat(result.output).contains("Generated \"MyIcons\" iconpack in package \"com.example.icons\"")
        assertThat(result.output).contains("Generated 2 ImageVector icons in nested pack \"Outlined\" (package: \"com.example.icons\")")
        assertThat(result.output).contains("Generated 2 ImageVector icons in nested pack \"Filled\" (package: \"com.example.icons\")")

        val expectedCode = """
            package com.example.icons

            object MyIcons {
                object Outlined

                object Filled
            }

        """.trimIndent()

        val iconPackFile = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/MyIcons.kt")
        assertThat(iconPackFile).exists()
        assertThat(iconPackFile.readText()).isEqualTo(expectedCode)

        // Count total generated files (1 pack file + 4 icon files)
        val generatedFiles = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedFiles.size).isEqualTo(5)

        // Verify all icons are in the flat package (not in nested packages)
        val flatPackageDir = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons")
        val iconFiles = flatPackageDir.toFile().listFiles().orEmpty().filter { it.name != "MyIcons.kt" }
        assertThat(iconFiles.size).isEqualTo(4)

        // Verify outlined icons are in the flat package
        val outlinedIconFiles = iconFiles.filter {
            it.readText().contains("val MyIcons.Outlined.")
        }
        assertThat(outlinedIconFiles.size).isEqualTo(2)

        outlinedIconFiles.forEach { file ->
            val content = file.readText()
            assertThat(content).contains("package com.example.icons")
            assertThat(content).contains("val MyIcons.Outlined.")
        }

        // Verify filled icons are in the flat package
        val filledIconFiles = iconFiles.filter {
            it.readText().contains("val MyIcons.Filled.")
        }
        assertThat(filledIconFiles.size).isEqualTo(2)

        filledIconFiles.forEach { file ->
            val content = file.readText()
            assertThat(content).contains("package com.example.icons")
            assertThat(content).contains("val MyIcons.Filled.")
        }

        // Verify no nested package directories were created
        val outlinedPackageDir = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/outlined")
        val filledPackageDir = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/com/example/icons/filled")
        assertThat(outlinedPackageDir).doesNotExist()
        assertThat(filledPackageDir).doesNotExist()
    }
}
