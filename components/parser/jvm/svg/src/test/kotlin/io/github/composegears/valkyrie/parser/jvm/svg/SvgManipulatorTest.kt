package io.github.composegears.valkyrie.parser.jvm.svg

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SvgManipulatorTest {

    @Test
    fun `modifySvg updates root element attribute`() {
        val originalSvg = """
            <svg width="24" height="24" viewBox="0 0 24 24">
                <path d="M10,10h4v4h-4z"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
            svgElement.setAttribute("height", "48")
        }

        assertThat(modifiedSvg).contains("width=\"48\"")
        assertThat(modifiedSvg).contains("height=\"48\"")
        assertThat(modifiedSvg).doesNotContain("width=\"24\"")
        assertThat(modifiedSvg).doesNotContain("height=\"24\"")
    }

    @Test
    fun `updateAttributeRecursively updates all matching attributes`() {
        val originalSvg = """
            <svg width="24" height="24" stroke-width="2">
                <g stroke-width="2">
                    <path stroke-width="2" d="M10,10h4v4h-4z"/>
                    <circle stroke-width="2" cx="12" cy="12" r="5"/>
                </g>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            SvgManipulator.updateAttributeRecursively(
                element = svgElement,
                attributeName = "stroke-width",
                newValue = "3",
            )
        }

        assertThat(modifiedSvg).contains("stroke-width=\"3\"")
        assertThat(modifiedSvg).doesNotContain("stroke-width=\"2\"")

        val occurrences = modifiedSvg.split("stroke-width=\"3\"").size - 1
        assertThat(occurrences).isEqualTo(4)
    }

    @Test
    fun `updateAttributeRecursively does not affect elements without attribute`() {
        val originalSvg = """
            <svg width="24" height="24" stroke-width="2">
                <path d="M10,10h4v4h-4z"/>
                <circle stroke-width="2" cx="12" cy="12" r="5"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            SvgManipulator.updateAttributeRecursively(
                element = svgElement,
                attributeName = "stroke-width",
                newValue = "3",
            )
        }

        assertThat(modifiedSvg).contains("<path d=\"M10,10h4v4h-4z\"/>")
        assertThat(modifiedSvg).contains("stroke-width=\"3\"")
    }

    @Test
    fun `updateAttributeConditionally only updates matching values`() {
        val originalSvg = """
            <svg stroke="currentColor">
                <path stroke="currentColor" d="M10,10h4v4h-4z"/>
                <circle stroke="#FF0000" cx="12" cy="12" r="5"/>
                <rect stroke="currentColor" x="0" y="0" width="10" height="10"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            SvgManipulator.updateAttributeConditionally(
                element = svgElement,
                attributeName = "stroke",
                currentValue = "currentColor",
                newValue = "#00FF00",
            )
        }

        assertThat(modifiedSvg).contains("stroke=\"#00FF00\"")
        assertThat(modifiedSvg).doesNotContain("stroke=\"currentColor\"")

        assertThat(modifiedSvg).contains("stroke=\"#FF0000\"")

        val greenCount = modifiedSvg.split("stroke=\"#00FF00\"").size - 1
        val redCount = modifiedSvg.split("stroke=\"#FF0000\"").size - 1
        assertThat(greenCount).isEqualTo(3)
        assertThat(redCount).isEqualTo(1)
    }

    @Test
    fun `modifySvg handles nested elements correctly`() {
        val originalSvg = """
            <svg width="24">
                <g>
                    <g>
                        <path stroke-width="2" d="M10,10h4v4h-4z"/>
                    </g>
                </g>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            SvgManipulator.updateAttributeRecursively(
                element = svgElement,
                attributeName = "stroke-width",
                newValue = "5",
            )
        }

        assertThat(modifiedSvg).contains("stroke-width=\"5\"")
        assertThat(modifiedSvg).doesNotContain("stroke-width=\"2\"")
    }

    @Test
    fun `modifySvg handles different quote styles`() {
        val originalSvg = """
            <svg width='24' height="24">
                <path d="M10,10h4v4h-4z"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
        }

        assertThat(modifiedSvg).contains("width=\"48\"")
    }

    @Test
    fun `modifySvg handles attributes with spaces`() {
        val originalSvg = """
            <svg width = "24" height= "24">
                <path d="M10,10h4v4h-4z"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
        }

        assertThat(modifiedSvg).contains("width=\"48\"")
    }

    @Test
    fun `modifySvg returns original content on parse failure`() {
        val invalidSvg = "This is not valid XML"

        val result = SvgManipulator.modifySvg(invalidSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
        }

        assertThat(result).isEqualTo(invalidSvg)
    }

    @Test
    fun `modifySvg preserves content structure`() {
        val originalSvg = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M10,10h4v4h-4z"/>
                <circle cx="12" cy="12" r="10"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
        }

        with(modifiedSvg) {
            assertTrue(contains("<svg"))
            assertTrue(contains("<path"))
            assertTrue(contains("<circle"))
            assertTrue(contains("</svg>"))
        }
    }

    @Test
    fun `updateAttributeRecursively handles empty attribute value`() {
        val originalSvg = """
            <svg width="24">
                <path stroke-width="" d="M10,10h4v4h-4z"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(originalSvg) { svgElement ->
            SvgManipulator.updateAttributeRecursively(
                element = svgElement,
                attributeName = "stroke-width",
                newValue = "2",
            )
        }

        assertThat(modifiedSvg).contains("stroke-width=\"2\"")
    }

    @Test
    fun `complex real-world Lucide icon manipulation`() {
        val lucideSvg = """
            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"/>
                <path d="M12 6v6l4 2"/>
            </svg>
        """.trimIndent()

        val modifiedSvg = SvgManipulator.modifySvg(lucideSvg) { svgElement ->
            svgElement.setAttribute("width", "48")
            svgElement.setAttribute("height", "48")

            SvgManipulator.updateAttributeRecursively(
                element = svgElement,
                attributeName = "stroke-width",
                newValue = "3",
            )

            SvgManipulator.updateAttributeConditionally(
                element = svgElement,
                attributeName = "stroke",
                currentValue = "currentColor",
                newValue = "#FF5733",
            )
        }

        with(modifiedSvg) {
            assertTrue(contains("width=\"48\""))
            assertTrue(contains("height=\"48\""))
            assertTrue(contains("stroke-width=\"3\""))
            assertTrue(contains("stroke=\"#FF5733\""))
            assertFalse(contains("stroke=\"currentColor\""))
            assertFalse(contains("stroke-width=\"2\""))
        }
    }
}
