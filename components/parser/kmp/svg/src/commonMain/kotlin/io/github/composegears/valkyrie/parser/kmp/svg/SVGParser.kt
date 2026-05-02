package io.github.composegears.valkyrie.parser.kmp.svg

import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.parser.common.PathParser

private typealias ViewBox = List<Float>

object SVGParser {

    fun parse(content: String): IrImageVector = SVGDeserializer.deserialize(content).toImageVector()

    private val ViewBox.width get() = get(2) - get(0)
    private val ViewBox.height get() = get(3) - get(1)

    // NOTE: width and height are defaulted to SVG sizing specs
    private const val DEFAULT_WIDTH = 300f
    private const val DEFAULT_HEIGHT = 150f

    private fun SVG.toImageVector(): IrImageVector {
        var width = width?.filter { !it.isLetter() }?.toFloat()
        var height = height?.filter { !it.isLetter() }?.toFloat()
        val rect: ViewBox? = viewBox?.split(" ")?.map { it.toFloat() }

        if (width == null && rect != null) {
            width = rect.width
        }
        if (height == null && rect != null) {
            height = rect.height
        }

        return IrImageVector(
            defaultWidth = width ?: DEFAULT_WIDTH,
            defaultHeight = height ?: DEFAULT_HEIGHT,
            viewportWidth = rect?.width ?: width ?: DEFAULT_WIDTH,
            viewportHeight = rect?.height ?: height ?: DEFAULT_HEIGHT,
            nodes = with(toPaintContext()) {
                children.mapNotNull { it.toIrVectorNode() }
            },
        )
    }

    private fun SVG.toPaintContext(): PaintContext = PaintContext(
        fill = fill,
        strokeColor = strokeColor,
        strokeWidth = strokeWidth,
        strokeLineCap = strokeLineCap,
        strokeLineJoin = strokeLineJoin,
        strokeAlpha = strokeAlpha,
        strokeMiter = strokeMiter,
    )

    context(paintContext: PaintContext)
    private fun SVG.Child.toIrVectorNode(): IrVectorNode? = when (this) {
        is SVG.Path -> toVectorPath()
        is SVG.Circle -> toVectorPath()
        is SVG.Polygon -> toVectorPath()
        is SVG.Group -> toVectorGroup()
        is SVG.Rectangle -> toVectorPath()
        is SVG.Ellipse -> toVectorPath()
    }

    context(paintContext: PaintContext)
    private fun SVG.Path.toVectorPath(): IrVectorNode.IrPath {
        val resolvedFill = fill ?: paintContext.fill
        var fillColor: IrColor? = resolvedFill?.let(SvgColorParser::parse)
        // NOTE: Only when fill and strokeColor is null use black FillColor as default color as
        //       fill can be none resulting to null.
        fillColor = if (resolvedFill == null && strokeColor == null) Black else fillColor
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            fill = fillColor?.let { IrFill.Color(it) },
            fillAlpha = fillAlpha?.toFloat() ?: 1f,
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

    context(paintContext: PaintContext)
    private fun SVG.Circle.toVectorPath(): IrVectorNode.IrPath {
        val cx = centerX.toFloat()
        val cy = centerY.toFloat()
        val r = radius.toFloat()
        val fill = resolveFill(fill)
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            fill = fill,
            fillAlpha = fillAlpha?.toFloat() ?: 1f,
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

    context(paintContext: PaintContext)
    private fun SVG.Polygon.toVectorPath(): IrVectorNode.IrPath {
        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            fill = resolveFill(fill),
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
                .orEmpty(),
        )
    }

    context(paintContext: PaintContext)
    private fun SVG.Group.toVectorGroup(): IrVectorNode.IrGroup {
        val groupContext = PaintContext(
            fill = fill ?: paintContext.fill,
            strokeColor = strokeColor ?: paintContext.strokeColor,
            strokeWidth = strokeWidth ?: paintContext.strokeWidth,
            strokeLineCap = strokeLineCap ?: paintContext.strokeLineCap,
            strokeLineJoin = strokeLineJoin ?: paintContext.strokeLineJoin,
            strokeAlpha = strokeAlpha ?: paintContext.strokeAlpha,
            strokeMiter = strokeMiter ?: paintContext.strokeMiter,
        )
        val pivot = transform?.getPivot() ?: Translation.Default
        val translation = transform?.getTranslation() ?: Translation.Default
        val scale = transform?.getScale() ?: Scale.Default
        return IrVectorNode.IrGroup(
            name = id.orEmpty(),
            nodes = with(groupContext) {
                children.mapNotNull { it.toIrVectorNode() }.toMutableList()
            },
            rotate = transform?.getRotation() ?: 0f,
            pivotX = pivot.x,
            pivotY = pivot.y,
            translationX = translation.x,
            translationY = translation.y,
            scaleX = scale.x,
            scaleY = scale.y,
            // TODO: Add missing clip path data
            clipPathData = mutableListOf(),
        )
    }

    context(paintContext: PaintContext)
    private fun SVG.Rectangle.toVectorPath(): IrVectorNode.IrPath? {
        val x = x.toFloat()
        val y = y.toFloat()
        val width = width?.toFloat() ?: return null
        val height = height?.toFloat() ?: return null
        if (width == 0f || height == 0f) return null

        val stroke = getSVGStrokeWithDefaults()
        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            pathFillType = IrPathFillType.NonZero,
            fill = resolveFill(fill),
            fillAlpha = 1f,
            stroke = stroke.color?.let { IrStroke.Color(it) },
            strokeAlpha = stroke.alpha,
            strokeLineMiter = stroke.miter,
            strokeLineCap = stroke.cap.toIrStrokeLineCap(),
            strokeLineJoin = stroke.join.toIrStrokeLineJoin(),
            strokeLineWidth = stroke.width,
            paths = listOf(
                IrPathNode.MoveTo(x = x, y = y),
                IrPathNode.RelativeHorizontalTo(x = width),
                IrPathNode.RelativeVerticalTo(y = height),
                IrPathNode.RelativeHorizontalTo(x = -width),
                IrPathNode.Close,
            ),
        )
    }

    context(paintContext: PaintContext)
    private fun SVG.Ellipse.toVectorPath(): IrVectorNode.IrPath {
        val cx = centerX.toFloat()
        val cy = centerY.toFloat()
        val radiusX = radiusX?.toFloat() ?: radiusY?.toFloat()
        requireNotNull(radiusX) { "either rx or ry must be set for ellipse $this" }
        val radiusY = radiusY?.toFloat() ?: radiusX
        val stroke = getSVGStrokeWithDefaults()

        return IrVectorNode.IrPath(
            name = id.orEmpty(),
            pathFillType = IrPathFillType.NonZero,
            fill = resolveFill(fill),
            fillAlpha = 1f,
            stroke = stroke.color?.let { IrStroke.Color(it) },
            strokeAlpha = stroke.alpha,
            strokeLineCap = stroke.cap.toIrStrokeLineCap(),
            strokeLineJoin = stroke.join.toIrStrokeLineJoin(),
            strokeLineWidth = stroke.width,
            strokeLineMiter = stroke.miter,
            paths = listOf(
                IrPathNode.MoveTo(x = cx - radiusX, y = cy),
                IrPathNode.ArcTo(
                    horizontalEllipseRadius = radiusX,
                    verticalEllipseRadius = radiusY,
                    theta = 0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    arcStartX = cx + radiusX,
                    arcStartY = cy,
                ),
                IrPathNode.ArcTo(
                    horizontalEllipseRadius = radiusX,
                    verticalEllipseRadius = radiusY,
                    theta = 0f,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    arcStartX = cx - radiusX,
                    arcStartY = cy,
                ),
                IrPathNode.Close,
            ),
        )
    }

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
        return functionStart.substring(1 until endIndex).split(" ", ",").map { it.toFloat() }
    }

    context(paintContext: PaintContext)
    private fun resolveFill(fill: String?): IrFill.Color? {
        val resolvedFill = fill ?: paintContext.fill
        val color = when (resolvedFill) {
            null -> Black
            else -> SvgColorParser.parse(resolvedFill)
        }
        return color?.let { IrFill.Color(it) }
    }

    @Suppress("PrivatePropertyName")
    private val Black: IrColor = IrColor(0xff000000)
}

internal data class PaintContext(
    val fill: String?,
    val strokeColor: String?,
    val strokeWidth: String?,
    val strokeLineCap: String?,
    val strokeLineJoin: String?,
    val strokeAlpha: String?,
    val strokeMiter: String?,
)
