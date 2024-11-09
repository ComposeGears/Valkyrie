package io.github.composegears.valkyrie.ir

val IR_STUB = IrImageVector(
    name = "",
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
