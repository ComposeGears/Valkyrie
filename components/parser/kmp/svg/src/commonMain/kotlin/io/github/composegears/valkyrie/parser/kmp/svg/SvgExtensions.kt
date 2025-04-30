package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin

internal fun SVG.Child.getSVGStrokeWithDefaults(): SVGStroke = SVGStroke(
    color = strokeColor?.let { SvgColorParser.parse(it) },
    alpha = strokeAlpha?.toFloat() ?: 1f,
    width = strokeWidth?.toFloat() ?: 0f,
    cap = strokeLineCap?.let { SVGStroke.Cap(it) } ?: SVGStroke.Cap.Butt,
    join = strokeLineJoin?.let { SVGStroke.Join(it) } ?: SVGStroke.Join.Miter,
    miter = strokeMiter?.toFloat() ?: 4f,
)

internal fun SVGStroke.Cap.toIrStrokeLineCap(): IrStrokeLineCap = when (this) {
    SVGStroke.Cap.Butt -> IrStrokeLineCap.Butt
    SVGStroke.Cap.Round -> IrStrokeLineCap.Round
    SVGStroke.Cap.Square -> IrStrokeLineCap.Square
}

internal fun SVGStroke.Join.toIrStrokeLineJoin(): IrStrokeLineJoin = when (this) {
    SVGStroke.Join.Bevel -> IrStrokeLineJoin.Bevel
    SVGStroke.Join.Miter -> IrStrokeLineJoin.Miter
    SVGStroke.Join.Round -> IrStrokeLineJoin.Round
}

internal fun String.getPathFillType(): IrPathFillType = when (this.lowercase()) {
    "evenodd" -> IrPathFillType.EvenOdd
    else -> IrPathFillType.NonZero
}
