package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.exists
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.common.GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.copyTo
import kotlin.io.path.createDirectories
import kotlin.io.path.readText
import kotlin.io.path.writeText
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class FullQualifiedNamesTest : CommonGradleTest() {

    @Test
    fun `test full qualified imports for Brush icon`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "test.icons"
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        val sourceDir = RESOURCES_DIR_XML.toPath()
        val resourceDir = root.resolve("src/commonMain/valkyrieResources")
        resourceDir.createDirectories()
        sourceDir.resolve("ic_brush.xml").copyTo(resourceDir.resolve("ic_brush.xml"))

        val result = runTask(root, TASK_NAME)

        assertThat(result.output).contains("Found icons names that conflict with reserved Compose qualifiers. Full qualified import will be used for: \"Brush\"")

        val ktPath = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/test/icons/Brush.kt")
        assertThat(ktPath).exists()
        assertThat(ktPath.readText()).doesNotContain("import androidx.compose.ui.graphics.Brush")
    }

    @Test
    fun `test full qualified imports for Color icon`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "test.icons"
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        val sourceDir = RESOURCES_DIR_XML.toPath()
        val resourceDir = root.resolve("src/commonMain/valkyrieResources")
        resourceDir.createDirectories()
        sourceDir.resolve("ic_brush.xml").copyTo(resourceDir.resolve("ic_color.xml"))

        val result = runTask(root, TASK_NAME)

        assertThat(result.output).contains("Found icons names that conflict with reserved Compose qualifiers. Full qualified import will be used for: \"Color\"")

        val ktPath = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/test/icons/Color.kt")
        assertThat(ktPath).exists()
        assertThat(ktPath.readText()).doesNotContain("import androidx.compose.ui.graphics.Color")
    }

    @Test
    fun `test full qualified imports for Offset icon`(@TempDir root: Path) {
        root.writeSettingsFile()

        root.resolve("build.gradle.kts").writeText(
            """
                plugins {
                    kotlin("multiplatform")
                    id("io.github.composegears.valkyrie")
                }
                valkyrie {
                    packageName = "test.icons"
                }
                kotlin {
                    jvm()
                }
            """.trimIndent(),
        )

        val sourceDir = RESOURCES_DIR_XML.toPath()
        val resourceDir = root.resolve("src/commonMain/valkyrieResources")
        resourceDir.createDirectories()
        sourceDir.resolve("ic_brush.xml").copyTo(resourceDir.resolve("ic_offset.xml"))

        val result = runTask(root, TASK_NAME)

        assertThat(result.output).contains("Found icons names that conflict with reserved Compose qualifiers. Full qualified import will be used for: \"Offset\"")

        val ktPath = root.resolve("${GENERATED_SOURCES_DIR}/commonMain/test/icons/Offset.kt")
        assertThat(ktPath).exists()
        assertThat(ktPath.readText()).doesNotContain("import androidx.compose.ui.graphics.Offset")
    }
}
