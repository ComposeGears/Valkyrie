package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin

context(paintContext: PaintContext)
internal fun SVG.Child.getSVGStrokeWithDefaults(): SVGStroke = SVGStroke(
    color = (strokeColor ?: paintContext.strokeColor)?.let { SvgColorParser.parse(it) },
    alpha = (strokeAlpha ?: paintContext.strokeAlpha)?.toFloat() ?: 1f,
    width = (strokeWidth ?: paintContext.strokeWidth)?.toFloat() ?: 0f,
    cap = (strokeLineCap ?: paintContext.strokeLineCap)?.let { SVGStroke.Cap(it) } ?: SVGStroke.Cap.Butt,
    join = (strokeLineJoin ?: paintContext.strokeLineJoin)?.let { SVGStroke.Join(it) } ?: SVGStroke.Join.Miter,
    miter = (strokeMiter ?: paintContext.strokeMiter)?.toFloat() ?: 4f,
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
