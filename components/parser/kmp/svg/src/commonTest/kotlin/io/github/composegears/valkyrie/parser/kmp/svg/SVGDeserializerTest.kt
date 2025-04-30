package io.github.composegears.valkyrie.parser.kmp.svg

import kotlin.test.Test
import kotlin.test.assertEquals

internal class SVGDeserializerTest {

    @Test
    fun `deserialize valid SVG file`() {
        val content = svg {
            """
            <path d="M0 0h24v24H0z" fill="none"/>
            <path d="M15.5 14h-.79l-.28-.27C15.41"/>
            """
        }

        assertEquals(
            actual = SVGDeserializer.deserialize(content),
            expected = testSVG(
                children = listOf(
                    SVG.Path(fillRule = "nonzero", pathData = "M0 0h24v24H0z", fill = "none"),
                    SVG.Path(fillRule = "nonzero", pathData = "M15.5 14h-.79l-.28-.27C15.41"),
                ),
            ),
        )
    }

    @Test
    fun `deserialize valid SVG file with fill rule evenodd`() {
        val content = svg {
            """
            <path d="M0 0h24v24H0z" fill="none" fill-rule="evenodd"/>
            <path d="M15.5 14h-.79l-.28-.27C15.41"/>
            """
        }

        val expected = testSVG(
            children = listOf(
                SVG.Path(fillRule = "evenodd", pathData = "M0 0h24v24H0z", fill = "none"),
                SVG.Path(fillRule = "nonzero", pathData = "M15.5 14h-.79l-.28-.27C15.41"),
            ),
        )

        assertEquals(actual = SVGDeserializer.deserialize(content), expected = expected)
    }

    @Test
    fun `parse SVG with groups`() {
        val content = svg(width = "1144.12px", height = "400px", viewBox = "0 0 572.06 200") {
            """
                <g id="bird">
            		<g id="body">
            			<path id="first" d="M48.42,78.11"/>
            		</g>
            		<g id="head">
            			<path id="second" d="M48.42,78.11"/>
            		</g>
            	</g>
            """
        }
        assertEquals(
            actual = SVGDeserializer.deserialize(content),
            expected = testSVG(
                width = "1144.12px",
                height = "400px",
                viewBox = "0 0 572.06 200",
                children = listOf(
                    SVG.Group(
                        id = "bird",
                        children = listOf(
                            SVG.Group(
                                id = "body",
                                children = listOf(SVG.Path(id = "first", pathData = "M48.42,78.11")),
                            ),
                            SVG.Group(
                                id = "head",
                                children = listOf(
                                    SVG.Path(
                                        id = "second",
                                        pathData = "M48.42,78.11",
                                    ),
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `parse svg with strokes`() {
        val svg = svg {
            """
            <path d="M12.5 12V7.5" stroke="#888888" stroke-width="1.5" stroke-linecap="butt" stroke-linejoin="round" stroke-opacity="0.5" stroke-miterlimit="1"/>
            """
        }
        assertEquals(
            actual = SVGDeserializer.deserialize(svg),
            expected = testSVG(
                children = listOf(
                    SVG.Path(
                        pathData = "M12.5 12V7.5",
                        strokeLineCap = "butt",
                        strokeLineJoin = "round",
                        strokeWidth = "1.5",
                        strokeColor = "#888888",
                        strokeAlpha = "0.5",
                        strokeMiter = "1",
                    ),
                ),
            ),
        )
    }

    @Test
    fun `parse svg with circle`() {
        val svg = svg { """<circle cx="50" cy="50" r="30" fill="green" fill-opacity="0.5" stroke="yellow" stroke-width="10"/>""" }
        assertEquals(
            actual = SVGDeserializer.deserialize(svg),
            expected = testSVG(
                children = listOf(
                    SVG.Circle(
                        centerX = "50",
                        centerY = "50",
                        radius = "30",
                        fill = "green",
                        fillAlpha = "0.5",
                        strokeColor = "yellow",
                        strokeWidth = "10",
                    ),
                ),
            ),
        )
    }

    @Test
    fun `parse SVG with rectangle`() {
        val svg = svg {
            """<rect width="150" height="50" fill="red" y="50" />"""
        }
        assertEquals(
            actual = SVGDeserializer.deserialize(svg),
            expected = testSVG(
                children = listOf(
                    SVG.Rectangle(
                        width = "150",
                        height = "50",
                        fill = "red",
                        y = "50",
                    ),
                ),
            ),
        )
    }

    @Test
    fun `parse SVG with mixed children order`() {
        val svg = svg {
            """
                <g id="head">
                </g>
                <rect width="150" height="50" />
                <g id="foot">
                </g>
            """.trimIndent()
        }

        assertEquals(
            actual = SVGDeserializer.deserialize(svg),
            expected = testSVG(
                children = listOf(
                    SVG.Group(id = "head"),
                    SVG.Rectangle(width = "150", height = "50"),
                    SVG.Group(id = "foot"),
                ),
            ),
        )
    }
}
