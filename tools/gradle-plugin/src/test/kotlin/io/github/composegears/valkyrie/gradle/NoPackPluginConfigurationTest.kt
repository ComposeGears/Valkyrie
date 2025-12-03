package io.github.composegears.valkyrie.gradle

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsMatch
import assertk.assertions.isEqualTo
import io.github.composegears.valkyrie.gradle.common.CommonGradleTest
import io.github.composegears.valkyrie.gradle.common.GENERATED_SOURCES_DIR
import io.github.composegears.valkyrie.gradle.internal.TASK_NAME
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlin.io.path.writeText
import org.gradle.testkit.runner.TaskOutcome.SKIPPED
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir

class NoPackPluginConfigurationTest : CommonGradleTest() {

    @Test
    fun `no pack generation configuration from commonMain sourceSet`(@TempDir root: Path) {
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

        assertThat(result).taskHadResult(":generateValkyrieImageVectorCommonTest", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmMain", SKIPPED)
        assertThat(result).taskHadResult(":generateValkyrieImageVectorJvmTest", SKIPPED)
        assertThat(result).taskWasSuccessful(":$TASK_NAME")
        assertThat(result.output).contains("Generated 17 ImageVector icons in package \"x.y.z\"")

        val generatedIcons = root.resolve(GENERATED_SOURCES_DIR).walk().toList()
        assertThat(generatedIcons.count()).isEqualTo(17)

        generatedIcons.forEach { file ->
            val relativePath = root.resolve(GENERATED_SOURCES_DIR).relativize(file)
            assertThat(relativePath.toString()).contains("commonMain/x/y/z/")

            assertThat(file.readText()).containsMatch(Regex("val [A-Za-z0-9_]+: ImageVector"))
        }
    }
}
