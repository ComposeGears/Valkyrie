package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
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
}
