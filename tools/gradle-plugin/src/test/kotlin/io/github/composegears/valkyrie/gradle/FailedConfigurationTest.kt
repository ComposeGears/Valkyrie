package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_RESOURCE_DIRECTORY
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class FailedConfigurationTest : CommonGradleTest() {

    @Test
    fun `Package name needs to be explicitly set if AGP isn't applied`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Couldn't automatically estimate package name")
    }

    @Test
    fun `blank packageName`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = ""
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("\"packageName\" cannot be blank")
    }

    @Test
    fun `incorrect codeStyle indentSize property`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    codeStyle {
                        indentSize = -1
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("indentSize must be non-negative, but was: \"-1\"")
    }

    @Test
    fun `missing iconpack name`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {

                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("iconPack \"name\" is required but not set")
    }

    @Test
    fun `blank iconpack name`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = ""
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("iconPack \"name\" cannot be blank")
    }

    @Test
    fun `missing iconpack targetSourceSet`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("iconPack \"targetSourceSet\" is required but not set")
    }

    @Test
    fun `missing nested pack name`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"

                        nested {
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("nested pack \"name\" is required but not set")
    }

    @Test
    fun `blank nested pack name`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"

                        nested {
                            name = ""
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("nested pack \"name\" cannot be blank")
    }

    @Test
    fun `missing nested pack sourceFolder`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"

                        nested {
                            name = "Outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("nested pack \"sourceFolder\" is required but not set")
    }

    @Test
    fun `blank nested pack sourceFolder`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "commonMain"

                        nested {
                            name = "Outlined"
                            sourceFolder = ""
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("nested pack \"sourceFolder\" cannot be blank")
    }

    @Test
    fun `nested pack duplicate name`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
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
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Duplicate nested pack name found: \"Outlined\"")
    }

    @Test
    fun `nested pack duplicate sourceFolder`(@TempDir root: Path) {
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
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
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Duplicate sourceFolder found: \"outlined\"")
    }

    @Test
    fun `case-insensitive duplicate icon names`(@TempDir root: Path) {
        root.writeSettingsFile()
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

        val svgDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY").createDirectories()

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // test-icon.svg -> TestIcon.kt
        svgDir.resolve("test-icon.svg").writeText(svgContent)
        // testicon.svg -> Testicon.kt
        // On case-insensitive file systems (macOS/Windows), TestIcon.kt and Testicon.kt collide
        svgDir.resolve("testicon.svg").writeText(svgContent)

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Found icon names that would collide on case-insensitive file systems")
        assertThat(result.output).contains("TestIcon")
        assertThat(result.output).contains("Testicon")
    }

    @Test
    fun `nested packs with flatPackage - exact duplicates across different nested packs`(@TempDir root: Path) {
        root.writeSettingsFile()
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "main"
                        useFlatPackage = true

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // Create same icon name in different nested packs
        val filledDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/filled").createDirectories()
        filledDir.resolve("test-icon.svg").writeText(svgContent)

        val outlinedDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/outlined").createDirectories()
        outlinedDir.resolve("test-icon.svg").writeText(svgContent)

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Found duplicate icon names in \"ValkyrieIcons\": TestIcon")
    }

    @Test
    fun `nested packs with flatPackage - case-insensitive duplicates across different nested packs`(@TempDir root: Path) {
        root.writeSettingsFile()
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "main"
                        useFlatPackage = true

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // Create case-insensitive duplicates in different nested packs
        val filledDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/filled").createDirectories()
        filledDir.resolve("test-icon.svg").writeText(svgContent)

        val outlinedDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/outlined").createDirectories()
        outlinedDir.resolve("testicon.svg").writeText(svgContent)

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Found icon names that would collide on case-insensitive file systems")
        assertThat(result.output).contains("ValkyrieIcons")
        assertThat(result.output).contains("TestIcon")
        assertThat(result.output).contains("Testicon")
    }

    @Test
    fun `nested packs without flatPackage - allow duplicates in different nested packs`(@TempDir root: Path) {
        root.writeSettingsFile()
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "main"
                        useFlatPackage = false

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // Create same icon name in different nested packs - should be OK without flatPackage
        val filledDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/filled").createDirectories()
        filledDir.resolve("test-icon.svg").writeText(svgContent)

        val outlinedDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/outlined").createDirectories()
        outlinedDir.resolve("test-icon.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
    }

    @Test
    fun `nested packs without flatPackage - detect duplicates within same nested pack`(@TempDir root: Path) {
        root.writeSettingsFile()
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "main"
                        useFlatPackage = false

                        nested {
                            name = "Filled"
                            sourceFolder = "filled"
                        }
                    }
                }
            """.trimIndent(),
        )

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // Create exact duplicates within same nested pack
        val filledDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/filled").createDirectories()
        filledDir.resolve("test-icon.svg").writeText(svgContent)
        filledDir.resolve("test_icon.svg").writeText(svgContent) // Same name after formatting

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Found duplicate icon names in \"ValkyrieIcons.Filled\": TestIcon")
    }

    @Test
    fun `nested packs without flatPackage - detect case-insensitive duplicates within same nested pack`(@TempDir root: Path) {
        root.writeSettingsFile()
        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("jvm")
                    id("io.github.composegears.valkyrie")
                }

                valkyrie {
                    packageName = "x.y.z"

                    iconPack {
                        name = "ValkyrieIcons"
                        targetSourceSet = "main"
                        useFlatPackage = false

                        nested {
                            name = "Outlined"
                            sourceFolder = "outlined"
                        }
                    }
                }
            """.trimIndent(),
        )

        val svgContent = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                <path d="M0 0h24v24H0z" fill="none"/>
            </svg>
        """.trimIndent()

        // Create case-insensitive duplicates within same nested pack
        val outlinedDir = root.resolve("src/main/$DEFAULT_RESOURCE_DIRECTORY/outlined").createDirectories()
        outlinedDir.resolve("test-icon.svg").writeText(svgContent)
        outlinedDir.resolve("testicon.svg").writeText(svgContent)

        val result = failTask(root, TASK_NAME)
        assertThat(result.output).contains("Found icon names that would collide on case-insensitive file systems")
        assertThat(result.output).contains("ValkyrieIcons.Outlined")
        assertThat(result.output).contains("TestIcon")
        assertThat(result.output).contains("Testicon")
    }
}
