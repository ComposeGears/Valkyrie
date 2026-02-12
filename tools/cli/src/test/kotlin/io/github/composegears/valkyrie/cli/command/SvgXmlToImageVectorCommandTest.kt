package io.github.composegears.valkyrie.cli.command

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.github.ajalt.clikt.testing.test
import io.github.composegears.valkyrie.cli.ext.outputError
import io.github.composegears.valkyrie.cli.ext.outputInfo
import io.github.composegears.valkyrie.sdk.test.resource.loader.ResourceLoader.getResourcePath
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockkStatic
import io.mockk.verify
import kotlin.io.path.absolutePathString
import kotlin.io.path.createTempDirectory
import kotlin.io.path.createTempFile
import kotlin.io.path.listDirectoryEntries
import kotlin.test.Test
import kotlin.test.assertFailsWith

class SvgXmlToImageVectorCommandTest {

    @Test
    fun `outputFormatOption should throw error for invalid value`() {
        mockkStatic(::outputError)
        every { outputError(any()) } answers { error("") }

        SvgXmlToImageVectorCommand().test(
            "--input-path",
            "/some/input",
            "--output-path",
            "/some/output",
            "--package-name",
            "com.example",
            "--output-format",
            "invalid-value",
        )

        verify { outputError("Invalid output format, must be backing-property or lazy-property") }
    }

    @Test
    fun `should throw error if input file is not SVG or XML`() {
        val mockMessage = "The input file must be an SVG or XML file."
        mockkStatic(::outputError)
        every { outputError(mockMessage) } answers { error(mockMessage) }

        val exception = assertFailsWith<IllegalStateException> {
            SvgXmlToImageVectorCommand().test(
                "--input-path",
                createTempFile().absolutePathString(),
                "--output-path",
                "/some/output",
                "--package-name",
                "com.example",
            )
        }
        assertThat(exception.message).isEqualTo(mockMessage)
    }

    @Test
    fun `should throw error if input path is not valid`() {
        val mockMessage = "The input path is not valid."
        mockkStatic(::outputError)
        every { outputError(mockMessage) } answers { error(mockMessage) }

        val exception = assertFailsWith<IllegalStateException> {
            SvgXmlToImageVectorCommand().test(
                "--input-path",
                "/invalid/path",
                "--output-path",
                "/some/output",
                "--package-name",
                "com.example",
            )
        }
        assertThat(exception.message).isEqualTo(mockMessage)
    }

    @Test
    fun `should throw error if no icons to process`() {
        val mockMessage = "No any icons to process"
        mockkStatic(::outputError)
        every { outputError(mockMessage) } answers { error(mockMessage) }

        val exception = assertFailsWith<IllegalStateException> {
            SvgXmlToImageVectorCommand().test(
                "--input-path",
                createTempDirectory().absolutePathString(),
                "--output-path",
                "/some/output",
                "--package-name",
                "com.example",
            )
        }
        assertThat(exception.message).isEqualTo(mockMessage)
    }

    @Test
    fun `should throw error if output path is not a directory`() {
        val mockMessage = "The output path must be a directory."
        mockkStatic(::outputError)
        every { outputError(mockMessage) } answers { error(mockMessage) }

        val exception = assertFailsWith<IllegalStateException> {
            SvgXmlToImageVectorCommand().test(
                "--input-path",
                getResourcePath("imagevector/svg/ic_linear_gradient.svg").absolutePathString(),
                "--output-path",
                createTempFile().absolutePathString(),
                "--package-name",
                "com.example",
            )
        }
        assertThat(exception.message).isEqualTo(mockMessage)
    }

    @Test
    fun `should log processing path for each file in verbose mode`() {
        mockkStatic(::outputInfo)
        every { outputInfo(any()) } just Runs

        val inputPath = getResourcePath("imagevector/svg")
        SvgXmlToImageVectorCommand().test(
            "--input-path",
            inputPath.absolutePathString(),
            "--output-path",
            createTempDirectory().absolutePathString(),
            "--package-name",
            "com.example",
            "--verbose",
        )

        inputPath.listDirectoryEntries("*.svg").forEach { path ->
            verify { outputInfo("process = $path") }
        }
    }
}
