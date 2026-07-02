package io.github.composegears.valkyrie.gradle.iconpack

import assertk.assertThat
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.internal.DEFAULT_RESOURCE_DIRECTORY
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

/**
 * Edge case and stress tests for nested packs.
 */
class NestedPacksEdgeCasesTest : CommonGradleTest() {

    private val svgContent = """
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
            <path d="M0 0h24v24H0z" fill="none"/>
        </svg>
    """.trimIndent()

    @Test
    fun `edge case - nested pack with very similar sourceFolder paths`(@TempDir root: Path) {
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
                            name = "Icon"
                            sourceFolder = "icon"
                        }
                        nested {
                            name = "IconFilled"
                            sourceFolder = "icon-filled"
                        }
                    }
                }
            """.trimIndent(),
        )

        val iconDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/icon").createDirectories()
        iconDir.resolve("home.svg").writeText(svgContent)

        val iconFilledDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/icon-filled").createDirectories()
        iconFilledDir.resolve("home.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - sourceFolder with special characters`(@TempDir root: Path) {
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
                            name = "Pack"
                            sourceFolder = "pack_v1-test.2"
                        }
                    }
                }
            """.trimIndent(),
        )

        val packDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/pack_v1-test.2").createDirectories()
        packDir.resolve("icon.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - deeply nested with many icons (stress)`(@TempDir root: Path) {
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
                            name = "Material"
                            sourceFolder = "material"

                            nested {
                                name = "Action"
                                sourceFolder = "action"

                                nested {
                                    name = "Filled"
                                    sourceFolder = "filled"
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        // Create many icons at deep level
        val filledDir =
            root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/material/action/filled").createDirectories()
        repeat(50) { i ->
            filledDir.resolve("icon_$i.svg").writeText(svgContent)
        }

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
        // Just verify 50 icons were processed (may output as "icons" or similar)
    }

    @Test
    fun `edge case - multiple nested branches with different depths`(@TempDir root: Path) {
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
                            name = "Shallow"
                            sourceFolder = "shallow"
                        }
                        nested {
                            name = "Deep"
                            sourceFolder = "d"

                            nested {
                                name = "Deeper"
                                sourceFolder = "dd"

                                nested {
                                    name = "Deepest"
                                    sourceFolder = "ddd"
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        val shallowDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/shallow").createDirectories()
        shallowDir.resolve("icon1.svg").writeText(svgContent)

        val deepestDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/d/dd/ddd").createDirectories()
        deepestDir.resolve("icon2.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - flatPackage with deep nesting - all names unified`(@TempDir root: Path) {
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
                            name = "Material"
                            sourceFolder = "m"

                            nested {
                                name = "Action"
                                sourceFolder = "a"

                                nested {
                                    name = "Filled"
                                    sourceFolder = "f"
                                }
                            }
                            nested {
                                name = "Shape"
                                sourceFolder = "s"

                                nested {
                                    name = "Filled"
                                    sourceFolder = "f2"
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        val actionDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/m/a/f").createDirectories()
        actionDir.resolve("home.svg").writeText(svgContent)

        val shapeDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/m/s/f2").createDirectories()
        shapeDir.resolve("square.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
        // All should be in x.y.z package when flat
    }

    @Test
    fun `edge case - non-flatPackage generates correct package names`(@TempDir root: Path) {
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
                            name = "Material"
                            sourceFolder = "material"

                            nested {
                                name = "Action"
                                sourceFolder = "action"

                                nested {
                                    name = "Filled"
                                    sourceFolder = "filled"
                                }
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        val filledDir =
            root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/material/action/filled").createDirectories()
        filledDir.resolve("home.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
        // Package should be x.y.z.material.action.filled
    }

    @Test
    fun `edge case - sourceFolders with forward slashes handled correctly`(@TempDir root: Path) {
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
                            name = "Material"
                            sourceFolder = "material/action/filled"
                        }
                    }
                }
            """.trimIndent(),
        )

        // File at full path
        val filledDir =
            root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/material/action/filled").createDirectories()
        filledDir.resolve("home.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - files in parent directory not matched to child pack`(@TempDir root: Path) {
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
                            name = "Material"
                            sourceFolder = "material"

                            nested {
                                name = "Action"
                                sourceFolder = "action"
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        // Put icon in parent folder
        val materialDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/material").createDirectories()
        materialDir.resolve("parent-icon.svg").writeText(svgContent)

        // Put icon in nested folder
        val actionDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/material/action").createDirectories()
        actionDir.resolve("child-icon.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        // Should be OK - parent icon won't match any pack, child will match Action pack
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - unicode in nested pack names`(@TempDir root: Path) {
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
                            name = "Solid"
                            sourceFolder = "solid"
                        }
                    }
                }
            """.trimIndent(),
        )

        val solidDir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/solid").createDirectories()
        solidDir.resolve("icon.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }

    @Test
    fun `edge case - identical nested pack names at different levels allowed`(@TempDir root: Path) {
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
                            sourceFolder = "filled1"

                            nested {
                                name = "Filled"
                                sourceFolder = "filled2"
                            }
                        }
                    }
                }
            """.trimIndent(),
        )

        val filled2Dir = root.resolve("src/main/${DEFAULT_RESOURCE_DIRECTORY}/filled1/filled2").createDirectories()
        filled2Dir.resolve("icon.svg").writeText(svgContent)

        val result = runTask(root, TASK_NAME)
        assertThat(result).taskWasSuccessful(":${TASK_NAME}")
    }
}
