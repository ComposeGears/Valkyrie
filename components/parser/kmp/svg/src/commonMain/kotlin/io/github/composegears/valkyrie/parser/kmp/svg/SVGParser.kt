package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.*
import io.github.composegears.valkyrie.parser.common.PathParser

object SvgColorParser {
    fun parse(colorValue: String): IrColor? {
        if (colorValue == "none") return null
        return KeywordColorParser.parse(colorValue) ?: IrColor(colorValue)
    }
}

object SVGParser {

    fun parse(content: String): Result<IrImageVector> =
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
        is SVG.Group -> toVectorGroup()
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
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id,
            fill = if (fill != null) SvgColorParser.parse(fill)?.let { IrFill.Color(it) } else IrFill.Color(Black),
            fillAlpha = fillAlpha?.toFloat() ?: 1f,
            stroke = stroke.color?.let { IrStroke.Color(it) },
            strokeAlpha = stroke.alpha,
            strokeLineWidth = stroke.width,
            strokeLineCap = stroke.cap.toIrStrokeLineCap(),
            strokeLineJoin = stroke.join.toIrStrokeLineJoin(),
            strokeLineMiter = stroke.miter,
            pathFillType = fillType?.getPathFillType() ?: IrPathFillType.NonZero,
            paths = points.takeIf { it.isNotEmpty() }
                ?.split(" ")
                ?.mapIndexed { i, pair ->
                    pair.split(",").let { (x, y) ->
                        if (i == 0) {
                            IrPathNode.MoveTo(x = x.toFloat(), y = y.toFloat())
                        } else {
                            IrPathNode.LineTo(x = x.toFloat(), y = y.toFloat())
                        }
                    }
                }
                ?.let { it + listOf(IrPathNode.Close) }
                .orEmpty()
        )
    }

    private fun SVG.Group.toVectorGroup(): IrVectorNode.IrGroup {
        val pivot = transform?.getPivot() ?: Translation.Default
        val translation = transform?.getTranslation() ?: Translation.Default
        val scale = transform?.getScale() ?: Scale.Default
        return IrVectorNode.IrGroup(
            name = name.orEmpty(),
            paths = children.map { it.toIrVectorNode() }.filterIsInstance<IrVectorNode.IrPath>().toMutableList(),
            rotate = transform?.getRotation() ?: 0f,
            pivotX = pivot.x,
            pivotY = pivot.y,
            translationX = translation.x,
            translationY = translation.y,
            scaleX = scale.x,
            scaleY = scale.y,
            // TODO: Add missing clip path data
            clipPathData = mutableListOf()
        )
    }

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
    private fun String.getRotation(): Float {
        return getFunction("rotate")
            ?.let { (a, _, _) -> a }
            ?: 0f
    }

    private fun String.getPivot(): Translation {
        return getFunction("rotate")?.let { Translation(it.drop(1)) } ?: Translation(listOf(0f, 0f))
    }

    private fun String.getTranslation(): Translation {
        return getFunction("translate")?.let { Translation(it) } ?: Translation(listOf(0f, 0f))
    }

    private fun String.getScale(): Scale {
        return getFunction("scale")?.let { Scale(args = it) } ?: Scale(listOf(1f, 1f))
    }

    private fun String.getFunction(key: String): List<Float>? {
        val startIndex = indexOf(key).takeIf { it != -1 } ?: return null
        val functionStart = substring(startIndex + key.length)
        val endIndex = functionStart.indexOfFirst { it == ')' }
        return functionStart.substring(1 until endIndex).split(" ").map { it.toFloat() }
    }

    @Suppress("PrivatePropertyName")
    private val Black: IrColor = IrColor(0xff000000)
}
