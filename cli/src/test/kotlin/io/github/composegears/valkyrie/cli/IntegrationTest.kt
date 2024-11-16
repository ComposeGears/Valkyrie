package io.github.composegears.valkyrie.cli

import io.github.composegears.valkyrie.extensions.ResourceUtils.getResourcePath
import io.github.composegears.valkyrie.generator.imagevector.OutputFormat
import java.nio.file.Path
import kotlin.io.path.createTempDirectory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class IntegrationTest {

    @ParameterizedTest
    @Disabled
    @MethodSource("testMatrix")
    fun exportAndImport(arg: Pair<Boolean, OutputFormat>) {
        val (useCli, outputFormat) = arg

        val inputXml = getResourcePath("/xml")
        val inputSvg = getResourcePath("/svg")
        val outputDir = tempDir.resolve("kt")
    }

    companion object {
        // Workaround for https://github.com/junit-team/junit5/issues/2811.
        @JvmStatic
        private lateinit var tempDir: Path

        @JvmStatic
        @BeforeAll
        fun before() {
            tempDir = createTempDirectory()
        }

        @JvmStatic
        @AfterAll
        fun after() {
            tempDir.toFile().delete()
        }

        @JvmStatic
        fun testMatrix() = listOf(
            false to OutputFormat.BackingProperty,
            false to OutputFormat.LazyProperty,
            true to OutputFormat.BackingProperty,
            true to OutputFormat.LazyProperty,
        )
    }
}
