package io.github.composegears.valkyrie.ir.util

import assertk.assertThat
import assertk.assertions.containsExactly
import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrVectorNode
import kotlin.test.Test

class IrImageVectorColorTest {

    @Test
    fun `extract ir colors`() {
        val red = IrColor(0xFFFF0000.toInt())
        val blue = IrColor(0xFF0000FF.toInt())
        val green = IrColor(0xFF00FF00.toInt())
        val yellow = IrColor(0xFFFFFF00.toInt())

        val vector = IrImageVector(
            name = "TestVector",
            defaultWidth = 24f,
            defaultHeight = 24f,
            viewportWidth = 24f,
            viewportHeight = 24f,
            nodes = listOf(
                IrVectorNode.IrPath(
                    fill = IrFill.Color(red),
                    stroke = IrStroke.Color(blue),
                    paths = emptyList(),
                ),
                IrVectorNode.IrGroup(
                    paths = mutableListOf(
                        IrVectorNode.IrPath(
                            fill = IrFill.LinearGradient(
                                startX = 0f,
                                startY = 0f,
                                endX = 10f,
                                endY = 10f,
                                colorStops = mutableListOf(
                                    IrFill.ColorStop(0f, green),
                                    IrFill.ColorStop(1f, blue),
                                ),
                            ),
                            paths = emptyList(),
                        ),
                    ),
                    clipPathData = mutableListOf(),
                ),
                IrVectorNode.IrPath(
                    fill = IrFill.RadialGradient(
                        centerX = 5f,
                        centerY = 5f,
                        radius = 10f,
                        colorStops = mutableListOf(
                            IrFill.ColorStop(0f, red),
                            IrFill.ColorStop(1f, yellow),
                        ),
                    ),
                    stroke = IrStroke.Color(blue),
                    paths = emptyList(),
                ),
            ),
        )

        val colors = vector.iconColors()
        assertThat(colors).containsExactly(red, blue, green, yellow)
    }
}
