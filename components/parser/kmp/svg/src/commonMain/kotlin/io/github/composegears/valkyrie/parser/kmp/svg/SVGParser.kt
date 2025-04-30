package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.ir.IrColor
import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrStrokeLineCap
import io.github.composegears.valkyrie.ir.IrStrokeLineJoin
import io.github.composegears.valkyrie.ir.IrVectorNode

fun interface ImageVectorParser {
    fun parse(content: String): Result<IrImageVector>
}

interface ColorParser {
    fun parse(color: String): IrColor?
}

// fun svgImageVectorParser(): ImageVectorParser = SVGParser()

internal class SVGParser(private val colorParser: ColorParser) : ImageVectorParser {

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
        is SVG.Group -> toVectorGroup()
        is SVG.Rectangle -> toVectorPath()
        is SVG.Ellipse -> toVectorPath()
        is SVG.Polygon -> toVectorPath()
    }

    private fun SVG.Path.toVectorPath(): IrVectorNode.IrPath {
        var fillColor: IrColor? = fill?.let(colorParser::parse)
        // NOTE: Only when fill and strokeColor is null use black FillColor as default color as
        //       fill can be none resulting to null.
        fillColor = if (fill == null && strokeColor == null) Black else fillColor
        return IrVectorNode.IrPath(
            name = id,
            fill = fillColor?.let { IrFill.Color(it) },
            fillAlpha = fillOpacity,
            stroke = strokeColor?.let(colorParser::parse)?.let(IrStroke::Color),
            strokeAlpha = strokeAlpha?.toFloat() ?: 1f,
            strokeLineWidth = strokeWidth?.toFloat() ?: 0f,
            strokeLineCap = strokeLinecap?.let(IrStrokeLineCap::valueOf) ?: IrStrokeLineCap.Butt,
            strokeLineJoin = strokeLinejoin?.let(IrStrokeLineJoin::valueOf) ?: IrStrokeLineJoin.Miter,
            strokeLineMiter = strokeMiter?.toFloat() ?: 4f,
            pathFillType =,
            paths = pathData,
        )
    }

//    private fun SVG.Circle.toVectorPath(): IrVectorNode.IrPath {
//        val cx = centerX.toFloat()
//        val cy = centerY.toFloat()
//        val r = radius.toFloat()
//        val color = fill?.let(colorParser::parse) ?: Black
//
//        return IrVectorNode.IrPath(
//            name = id.orEmpty(),
//                fill = IrFill,
//                fillAlpha = ,
//                stroke = ,
//                strokeAlpha = ,
//                strokeLineWidth = ,
//                strokeLineCap = ,
//                strokeLineJoin = ,
//                strokeLineMiter = ,
//                pathFillType = ,
//                paths = ,
//
//            fillColor = color,
//            commands = listOf(
//                MoveTo(x = cx - r, y = cy),
//                ArcTo(
//                    horizontalEllipseRadius = r,
//                    verticalEllipseRadius = r,
//                    theta = 0f,
//                    isMoreThanHalf = false,
//                    isPositiveArc = true,
//                    x1 = cx + r,
//                    y1 = cy,
//                ),
//                ArcTo(
//                    horizontalEllipseRadius = r,
//                    verticalEllipseRadius = r,
//                    theta = 0f,
//                    isMoreThanHalf = false,
//                    isPositiveArc = true,
//                    x1 = cx - r,
//                    y1 = cy,
//                ),
//                Close(),
//            ),
//            alpha = opacity.toFloat(),
//            stroke = Stroke(),
//        )
//    }
//
//    private fun SVG.Polygon.toVectorPath(): IrVectorNode.IrPath {
//        return IrVectorNode.IrPath(
//            name = "",
//            fill = IrFill.Color(irColor =),
//            fillAlpha = alpha?.toFloatOrNull() ?: 1f,
//            stroke =,
//            strokeAlpha = 1f,
//            strokeLineWidth = strokeWidth?.toFloat() ?: 0f,
//            strokeLineCap =,
//            strokeLineJoin =,
//            strokeLineMiter =,
//            pathFillType =,
//            paths = points.takeIf { it.isNotEmpty() }?.split(" ")
//                ?.let { listOf(IrPathNode.MoveTo(it), IrPathNode.Close) }.orEmpty(),
//        )
//    }
//
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
