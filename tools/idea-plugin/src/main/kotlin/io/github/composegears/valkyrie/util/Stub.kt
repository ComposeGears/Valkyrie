package io.github.composegears.valkyrie.util

import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

val IR_STUB = IrImageVector(
    defaultWidth = 16.0f,
    defaultHeight = 16.0f,
    viewportWidth = 16.0f,
    viewportHeight = 16.0f,
    nodes = listOf(
        IrVectorNode.IrPath(
            stroke = IrStroke.Color(irColor = IrColor("0xFF000000")),
            strokeLineWidth = 2f,
            strokeLineCap = IrStrokeLineCap.Round,
            paths = listOf(
                IrPathNode.MoveTo(x = 2.5f, y = 13.5f),
                IrPathNode.LineTo(x = 13.5f, y = 2.5f),
                IrPathNode.MoveTo(x = 13.5f, y = 13.5f),
                IrPathNode.LineTo(x = 2.5f, y = 2.5f),
            ),
        ),
    ),
)
