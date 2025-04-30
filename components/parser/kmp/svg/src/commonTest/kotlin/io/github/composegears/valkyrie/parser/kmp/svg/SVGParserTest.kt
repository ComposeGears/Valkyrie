package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrPathNode
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class SVGParserTest {

    private val IrImageVector.groups: List<IrVectorNode.IrGroup>
        get() = nodes.filterIsInstance<IrVectorNode.IrGroup>()

    @Test
    fun svg_without_viewBox_uses_width_and_height() {
        val svg = svg(viewBox = null) { "" }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = emptyList(),
            ),
        )
    }

    @Test
    fun svg_with_invalid_viewBox_throws_IllegalArgumentException() {
        val svg = svg(viewBox = "r4nd0m") { "" }
        assertFailsWith<IllegalArgumentException> {
            SVGParser.parse(svg)
        }
    }

    @Test
    fun parse_path_with_stroke_from_SVG() {
        val svg = svg {
            """<path d="" stroke="black" stroke-width="2" stroke-linecap="square" stroke-linejoin="round" stroke-miterlimit="0.25" stroke-opacity="0.5"/>"""
        }
        assertEquals(
            actual = SVGParser.parse(svg).nodes,
            expected = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.NonZero,
                    fill = null,
                    paths = emptyList(),
                    fillAlpha = 1f,
                    stroke = IrStroke.Color(IrColor(0xFF000000)),
                    strokeAlpha = .5f,
                    strokeLineWidth = 2f,
                    strokeLineCap = IrStrokeLineCap.Square,
                    strokeLineJoin = IrStrokeLineJoin.Round,
                    strokeLineMiter = .25f,
                ),
            ),
        )
    }

    @Test
    fun parse_SVG_file_with_correct_fill_color() {
        val svg = svg {
            """
            <path d="" fill="none"/>
            <path fill="#fff" d=""/>
            <path d=""/>
            """
        }
        val expected = listOf(
            IrVectorNode.IrPath(
                pathFillType = IrPathFillType.NonZero,
                fill = null,
                paths = emptyList(),
                fillAlpha = 1f,
            ),
            IrVectorNode.IrPath(
                pathFillType = IrPathFillType.NonZero,
                fill = IrFill.Color(IrColor(0xFFFFFFFF)),
                paths = emptyList(),
                fillAlpha = 1f,
            ),
            IrVectorNode.IrPath(
                pathFillType = IrPathFillType.NonZero,
                fill = IrFill.Color(IrColor(0xFF000000)),
                paths = emptyList(),
                fillAlpha = 1f,
            ),
        )
        assertEquals(
            actual = SVGParser.parse(content = svg).nodes,
            expected = expected,
        )
    }

    @Test
    fun parse_file_with_group_rotation() {
        val svg = svg { """<g transform="rotate(45 0 0)"/>""" }
        assertEquals(
            actual = SVGParser.parse(svg).nodes,
            expected = listOf(
                IrVectorNode.IrGroup(
                    name = "",
                    clipPathData = mutableListOf(),
                    nodes = mutableListOf(),
                    rotate = 45f,
                    pivotX = 0f,
                    pivotY = 0f,
                    translationX = 0f,
                    translationY = 0f,
                    scaleX = 1f,
                    scaleY = 1f,
                ),
            ),
        )
    }

    @Test
    fun parse_file_with_group_pivot() {
        val svg = svg { """<g transform="rotate(45 20 30)"/>""" }
        assertEquals(
            actual = SVGParser.parse(svg).groups.map { it.pivotX to it.pivotY },
            expected = listOf(20f to 30f),
        )
    }

    @Test
    fun parse_file_with_group_translate() {
        val svg = svg { """<g transform="translate(20 30)"/>""" }
        assertEquals(
            actual = SVGParser.parse(svg).groups.map { it.translationX to it.translationY },
            expected = listOf(20f to 30f),
        )
    }

    @Test
    fun parse_path_data_with_fill_none() {
        val svg = svg { """<path d="" fill="none"/>""" }

        assertEquals(
            actual = SVGParser.parse(svg).nodes,
            expected = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.NonZero,
                    fill = null,
                    paths = emptyList(),
                    fillAlpha = 1f,
                ),
            ),
        )
    }

    @Test
    fun parse_invalid_ellipse_from_SVG() {
        val svg = svg { """<ellipse cx="10" /> """ }
        assertFailsWith<IllegalArgumentException> { SVGParser.parse(svg) }
    }

    @Test
    fun parse_ellipse_from_SVG() {
        val svg = svg {
            """<ellipse cx="10" cy="20" rx="10" ry="20" fill="red" stroke="blue" stroke-width="3"/>"""
        }
        assertEquals(
            actual = SVGParser.parse(svg).nodes,
            expected = listOf(
                IrVectorNode.IrPath(
                    pathFillType = IrPathFillType.NonZero,
                    fill = IrFill.Color(IrColor(0xFFFF0000)),
                    fillAlpha = 1f,
                    stroke = IrStroke.Color(
                        IrColor(0xFF0000FF),
                    ),
                    strokeLineWidth = 3f,
                    strokeAlpha = 1f,
                    paths = listOf(
                        IrPathNode.MoveTo(0f, 20f),
                        IrPathNode.ArcTo(
                            horizontalEllipseRadius = 10f,
                            verticalEllipseRadius = 20f,
                            theta = 0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            arcStartX = 20f,
                            arcStartY = 20f,
                        ),
                        IrPathNode.ArcTo(
                            horizontalEllipseRadius = 10f,
                            verticalEllipseRadius = 20f,
                            theta = 0f,
                            isMoreThanHalf = false,
                            isPositiveArc = true,
                            arcStartX = 0f,
                            arcStartY = 20f,
                        ),
                        IrPathNode.Close,
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_circle_from_SVG() {
        val svg = svg { """<circle cx="5" cy="5" r="5" fill="#00ff00" />""" }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.Color(IrColor(0xFF00FF00)),
                        paths = listOf(
                            IrPathNode.MoveTo(x = 0f, y = 5f),
                            IrPathNode.ArcTo(
                                horizontalEllipseRadius = 5f,
                                verticalEllipseRadius = 5f,
                                theta = 0f,
                                isMoreThanHalf = false,
                                isPositiveArc = true,
                                arcStartX = 10f,
                                arcStartY = 5f,
                            ),
                            IrPathNode.ArcTo(
                                horizontalEllipseRadius = 5f,
                                verticalEllipseRadius = 5f,
                                theta = 0f,
                                isMoreThanHalf = false,
                                isPositiveArc = true,
                                arcStartX = 0f,
                                arcStartY = 5f,
                            ),
                            IrPathNode.Close,
                        ),
                        fillAlpha = 1f,
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_rect_from_SVG() {
        val svg = svg { """<rect x="1" y="2" width="10" height="5" />""" }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.Color(IrColor(0xff000000)),
                        paths = listOf(
                            IrPathNode.MoveTo(x = 1f, y = 2f),
                            IrPathNode.LineTo(x = 11f, y = 2f),
                            IrPathNode.LineTo(x = 11f, y = 7f),
                            IrPathNode.LineTo(x = 1f, y = 7f),
                            IrPathNode.Close,
                        ),
                        fillAlpha = 1f,
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_rect_without_width_and_height_from_SVG() {
        val svg = svg { """<rect x="1" y="2" />""" }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.NonZero,
                        fill = IrFill.Color(IrColor(0xff000000)),
                        paths = listOf(
                            IrPathNode.MoveTo(x = 1f, y = 2f),
                            IrPathNode.LineTo(x = 1f, y = 2f),
                            IrPathNode.LineTo(x = 1f, y = 2f),
                            IrPathNode.LineTo(x = 1f, y = 2f),
                            IrPathNode.Close,
                        ),
                        fillAlpha = 1f,
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_minimal_polygon_from_SVG() {
        val svg = svg { """<polygon points="0,0 10,10, 0,10" />""" }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        fill = IrFill.Color(IrColor(0xFF000000)),
                        paths = listOf(
                            IrPathNode.MoveTo(0f, 0f),
                            IrPathNode.LineTo(10f, 10f),
                            IrPathNode.LineTo(0f, 10f),
                            IrPathNode.Close,
                        ),
                        fillAlpha = 1f,
                        stroke = null,
                    ),
                ),
            ),
        )
    }

    @Test
    fun parse_polygon_from_SVG() {
        val svg = svg {
            """<polygon points="0,0 10,10, 0,10" fill="none" fill-rule="evenodd" fill-opacity="0.5" stroke="black" stroke-width="3" />"""
        }
        assertEquals(
            actual = SVGParser.parse(svg),
            expected = IrImageVector(
                defaultWidth = 24f,
                defaultHeight = 24f,
                viewportWidth = 24f,
                viewportHeight = 24f,
                nodes = listOf(
                    IrVectorNode.IrPath(
                        pathFillType = IrPathFillType.EvenOdd,
                        fill = null,
                        paths = listOf(
                            IrPathNode.MoveTo(0f, 0f),
                            IrPathNode.LineTo(10f, 10f),
                            IrPathNode.LineTo(0f, 10f),
                            IrPathNode.Close,
                        ),
                        fillAlpha = .5f,
                        stroke = IrStroke.Color(IrColor(0xFF000000)),
                        strokeLineWidth = 3f,
                    ),
                ),
            ),
        )
    }
}
