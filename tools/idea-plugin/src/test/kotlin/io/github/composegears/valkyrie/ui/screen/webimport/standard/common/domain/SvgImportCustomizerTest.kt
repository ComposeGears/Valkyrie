package io.github.composegears.valkyrie.ui.screen.webimport.standard.common.domain

import assertk.assertThat
import assertk.assertions.contains
import io.github.composegears.valkyrie.ui.screen.webimport.standard.common.model.SvgImportSettings
import org.junit.jupiter.api.Test

class SvgImportCustomizerTest {

    @Test
    fun `applySettings uses viewBox center for rotation`() {
        val svg = """
            <svg width="24" height="24" viewBox="10 20 24 24"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(rotation = 90),
        )
        assertThat(result).contains("transform=\"rotate(90 22.0 32.0)\"")
    }

    @Test
    fun `applySettings uses viewBox bounds for horizontal flip`() {
        val svg = """
            <svg width="24" height="24" viewBox="10 20 24 24"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(flipHorizontally = true),
        )
        assertThat(result).contains("transform=\"translate(44.0 0) scale(-1 1)\"")
    }

    @Test
    fun `applySettings uses viewBox bounds for vertical flip`() {
        val svg = """
            <svg width="24" height="24" viewBox="10 20 24 24"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(flipVertically = true),
        )
        assertThat(result).contains("transform=\"translate(0 64.0) scale(1 -1)\"")
    }

    @Test
    fun `applySettings falls back to width and height when viewBox is missing`() {
        val svg = """
            <svg width="24" height="24"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(
                rotation = 90,
                flipHorizontally = true,
                flipVertically = true,
            ),
        )

        assertThat(result).apply {
            contains("rotate(90 12.0 12.0)")
            contains("translate(24.0 0) scale(-1 1)")
            contains("translate(0 24.0) scale(1 -1)")
        }
    }

    @Test
    fun `applySettings updates currentColor fill and stroke`() {
        val svg = """
            <svg width="24" height="24" fill="currentColor" stroke="currentColor"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(color = "#6913E0"),
        )
        assertThat(result).apply {
            contains("color=\"#6913E0\"")
            contains("fill=\"#6913E0\"")
            contains("stroke=\"#6913E0\"")
        }
    }

    @Test
    fun `applySettings sets fill when color is provided and fill is absent`() {
        val svg = """
            <svg width="24" height="24"></svg>
        """.trimIndent()

        val result = SvgImportCustomizer.applySettings(
            svgContent = svg,
            settings = SvgImportSettings(color = "#FFFFFF"),
        )
        assertThat(result).contains("fill=\"#FFFFFF\"")
    }
}
