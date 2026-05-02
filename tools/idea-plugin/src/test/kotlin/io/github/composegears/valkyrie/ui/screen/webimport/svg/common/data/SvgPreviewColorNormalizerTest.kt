package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import org.junit.jupiter.api.Test

class SvgPreviewColorNormalizerTest {

    @Test
    fun `normalize currentColor to mask color for grid preview`() {
        val svg = """
            <svg fill="none" stroke="currentColor">
                <path stroke="currentColor" d="M4 4h16"/>
            </svg>
        """.trimIndent()

        val normalized = SvgPreviewColorNormalizer.normalize(svg)

        assertThat(normalized).contains("stroke=\"#000000\"")
        assertThat(normalized).doesNotContain("currentColor")
    }

    @Test
    fun `apply inherited root currentColor fill to paths for grid preview`() {
        val svg = """
            <svg viewBox="0 0 24 24" fill="currentColor">
                <path d="M4 4h16"/>
            </svg>
        """.trimIndent()

        val normalized = SvgPreviewColorNormalizer.normalize(svg)

        assertThat(normalized).contains("<path fill=\"#000000\" d=\"M4 4h16\"")
        assertThat(normalized).doesNotContain("currentColor")
    }

    @Test
    fun `apply inherited root currentColor stroke to paths for grid preview`() {
        val svg = """
            <svg fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" d="M4 4h16"/>
            </svg>
        """.trimIndent()

        val normalized = SvgPreviewColorNormalizer.normalize(svg)

        assertThat(normalized).contains("<path stroke=\"#000000\" stroke-linecap=\"round\" d=\"M4 4h16\"")
        assertThat(normalized).doesNotContain("currentColor")
    }
}
