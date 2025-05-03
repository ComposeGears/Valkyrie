package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.*
import io.github.composegears.valkyrie.parser.common.PathParser

interface ImageVectorParser {
    fun parse(content: String): Result<IrImageVector>
}

object SvgColorParser {
    fun parse(colorValue: String): IrColor? {
        if (colorValue == "none") return null
        return KeywordColorParser.parse(colorValue) ?: IrColor(colorValue)
    }
}

object SVGParser : ImageVectorParser {

    override fun parse(content: String): Result<IrImageVector> =
        runCatching { SVGDeserializer.deserialize(content).toImageVector() }

    private fun SVG.toImageVector(): IrImageVector {
        val width = width.filter { it.isDigit() }.toFloat()
        val height = height.filter { it.isDigit() }.toFloat()
        val rect: List<Float> = viewBox?.split(" ")
            ?.map { it.toFloat() }
            ?: listOf(0f, 0f, width, height)
        return IrImageVector(
            defaultWidth = width,
            defaultHeight = height,
            viewportWidth = rect[2] - rect[0],
            viewportHeight = rect[3] - rect[1],
            nodes = children.map { it.toIrVectorNode() },
        )
    }

    private fun SVG.Child.toIrVectorNode(): IrVectorNode = when (this) {
        is SVG.Path -> toVectorPath()
        is SVG.Circle -> toVectorPath()
        is SVG.Polygon -> toVectorPath()
        is SVG.Group -> TODO()
        is SVG.Rectangle -> TODO()
        is SVG.Ellipse -> TODO()
    }

    private fun SVG.Path.toVectorPath(): IrVectorNode.IrPath {
        var fillColor: IrColor? = fill?.let(SvgColorParser::parse)
        // NOTE: Only when fill and strokeColor is null use black FillColor as default color as
        //       fill can be none resulting to null.
        fillColor = if (fill == null && strokeColor == null) Black else fillColor
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id,
            fill = fillColor?.let { IrFill.Color(it) },
            fillAlpha = fillOpacity,
            stroke = stroke.color?.let(IrStroke::Color),
            strokeAlpha = stroke.alpha,
            strokeLineWidth = stroke.width,
            strokeLineCap = stroke.cap.toIrStrokeLineCap(),
            strokeLineJoin = stroke.join.toIrStrokeLineJoin(),
            strokeLineMiter = stroke.miter,
            pathFillType = fillRule.getPathFillType(),
            paths = PathParser.parsePathString(pathData),
        )
    }

    private fun SVG.Circle.toVectorPath(): IrVectorNode.IrPath {
        val cx = centerX.toFloat()
        val cy = centerY.toFloat()
        val r = radius.toFloat()
        val color = fill?.let(SvgColorParser::parse) ?: Black
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            fill = IrFill.Color(color),
            fillAlpha = fillAlpha.toFloat(),
            stroke = stroke.color?.let { IrStroke.Color(it) },
            strokeAlpha = stroke.alpha,
            strokeLineWidth = stroke.width,
            strokeLineCap = stroke.cap.toIrStrokeLineCap(),
            strokeLineJoin = stroke.join.toIrStrokeLineJoin(),
            strokeLineMiter = stroke.miter,
            pathFillType = fillRule?.getPathFillType() ?: IrPathFillType.NonZero,
            paths = listOf(
                IrPathNode.MoveTo(x = cx - r, y = cy),
                IrPathNode.ArcTo(
                    horizontalEllipseRadius = r,
                    verticalEllipseRadius = r,
                    theta = 0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    arcStartX = cx + r,
                    arcStartY = cy,
                ),
                IrPathNode.ArcTo(
                    horizontalEllipseRadius = r,
                    verticalEllipseRadius = r,
                    theta = 0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    arcStartX = cx - r,
                    arcStartY = cy,
                ),
                IrPathNode.Close,
            ),
        )
    }

    private fun SVG.Polygon.toVectorPath(): IrVectorNode.IrPath {
        return IrVectorNode.IrPath(
            name = id,
            fill = this.fill?.let { SvgColorParser.parse(it) }?.let { IrFill.Color(it) },
            fillAlpha = alpha?.toFloat() ?: 1f,
//            stroke =,
//            strokeAlpha = 1f,
//            strokeLineWidth = strokeWidth?.toFloat() ?: 0f,
//            strokeLineCap =,
//            strokeLineJoin =,
//            strokeLineMiter =,
//            pathFillType =,
            paths = points.takeIf { it.isNotEmpty() }
                ?.split(" ")
                ?.map { it.split(",").let { (x, y) -> IrPathNode.MoveTo(x = x.toFloat(), y = y.toFloat()) } }
                ?.let { it + listOf(IrPathNode.Close) }
                .orEmpty()
        )
    }

//    private fun SVG.Group.toVectorGroup(): IrVectorNode.IrGroup {
//        return IrVectorNode.IrGroup(
//            name = name.orEmpty(),
//            paths = children.map { it.toNode() },
//            rotate = transform?.getRotation() ?: 0f,
//            pivot = transform?.getPivot() ?: Translation(0f, 0f),
//            translation = transform?.getTranslation() ?: Translation(0f, 0f),
//            scale = transform?.getScale() ?: Scale(1f, 1f),
//        )
//    }
//
//    private fun SVG.Ellipse.toVectorPath(): IrVectorNode.IrPath {
//        val cx = centerX.toFloat()
//        val cy = centerY.toFloat()
//        val radiusX = radiusX?.toFloat() ?: radiusY?.toFloat()
//        requireNotNull(radiusX) { "either rx or ry must be set for ellipse $this" }
//        val radiusY = radiusY?.toFloat() ?: radiusX
//
//        return IrVectorNode.IrPath(
//            fillType = FillType.Default,
//            fillColor = fill?.let { colorParser.parse(it) },
//            stroke = Stroke(
//                color = stroke?.let { colorParser.parse(it) },
//                alpha = if (stroke != null) 1f else 0f,
//                width = strokeWidth?.toFloat() ?: if (stroke != null) 1f else 0f,
//                cap = strokeLineCap?.let { Cap(it) } ?: Cap.Butt,
//                join = strokeLineJoin?.let { Join(it) } ?: Join.Bevel,
//                miter = strokeMiterLimit?.toFloat() ?: 1f,
//            ),
//            alpha = 1f,
//            commands = listOf(
//                MoveTo(x = cx - radiusX, y = cy),
//                ArcTo(
//                    horizontalEllipseRadius = radiusX,
//                    verticalEllipseRadius = radiusY,
//                    theta = 0f,
//                    isMoreThanHalf = false,
//                    isPositiveArc = true,
//                    x1 = cx + radiusX,
//                    y1 = cy,
//                ),
//                ArcTo(
//                    horizontalEllipseRadius = radiusX,
//                    verticalEllipseRadius = radiusY,
//                    theta = 0f,
//                    isMoreThanHalf = false,
//                    isPositiveArc = true,
//                    x1 = cx - radiusX,
//                    y1 = cy,
//                ),
//                Close(),
//            ),
//        )
//    }
//
//    private fun SVG.Rectangle.toVectorPath(): IrVectorNode.IrPath {
//        val x = x.toFloat()
//        val y = y.toFloat()
//        val width = width?.toFloat() ?: 0f
//        val height = height?.toFloat() ?: 0f
//        return IrVectorNode.IrPath(
//            fillType = FillType.Default,
//            fillColor = fill?.let(colorParser::parse),
//            alpha = 1f,
//            stroke = Stroke(width = 0f),
//            commands = listOf(
//                MoveTo(x = x, y = y),
//                LineTo(x = x + width, y = y),
//                LineTo(x = x + width, y = y + height),
//                LineTo(x = x, y = y + height),
//                Close(),
//            ),
//        )
//    }
//
//    private fun String.getRotation(): Float {
//        return getFunction("rotate")
//            ?.let { (a, _, _) -> a }
//            ?: 0f
//    }
//
//    private fun String.getPivot(): Translation {
//        return getFunction("rotate")
//            ?.let { (_, x, y) -> Translation(x = x, y = y) }
//            ?: Translation(0f, 0f)
//    }
//
//    private fun String.getTranslation(): Translation {
//        return getFunction("translate")
//            ?.let { (x, y) -> Translation(x = x, y = y) }
//            ?: Translation(0f, 0f)
//    }
//
//    private fun String.getScale(): Scale {
//        return getFunction("scale")
//            ?.let { (x, y) -> Scale(x = x, y = y) }
//            ?: Scale(1f, 1f)
//    }
//
//    private fun String.getFunction(key: String): List<Float>? {
//        val startIndex = indexOf(key).takeIf { it != -1 } ?: return null
//        val functionStart = substring(startIndex + key.length)
//        val endIndex = functionStart.indexOfFirst { it == ')' }
//        return functionStart.substring(1 until endIndex).split(" ").map { it.toFloat() }
//    }

    @Suppress("PrivatePropertyName")
    private val Black: IrColor = IrColor(0xff000000)
}

internal fun SVG.Child.getSVGStrokeWithDefaults(): SVGStroke {
    return SVGStroke(
        color = strokeColor?.let { SvgColorParser.parse(it) },
        alpha = strokeAlpha?.toFloat() ?: 1f,
        width = strokeWidth?.toFloat() ?: 0f,
        cap = strokeLineCap?.let { SVGStroke.Cap(it) } ?: SVGStroke.Cap.Butt,
        join = strokeLineJoin?.let { SVGStroke.Join(it) } ?: SVGStroke.Join.Miter,
        miter = strokeMiter?.toFloat() ?: 4f,
    )
}

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
