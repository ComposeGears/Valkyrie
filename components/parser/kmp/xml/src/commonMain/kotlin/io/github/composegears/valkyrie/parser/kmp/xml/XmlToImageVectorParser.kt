package io.github.composegears.valkyrie.parser.kmp.xml

import io.github.composegears.valkyrie.parser.common.PathParser
import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

object XmlToImageVectorParser {
    fun parse(text: String): IrImageVector {
        return XmlDeserializer.deserialize(text).toIrImageVector()
    }

    private fun VectorDrawable.toIrImageVector(): IrImageVector {
        return IrImageVector(
            name = name.orEmpty(),
            autoMirror = autoMirrored,
            defaultWidth = widthInDp.removeSuffix("dp").toFloat(),
            defaultHeight = heightInDp.removeSuffix("dp").toFloat(),
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            nodes = children.map { it.toNode() },
        )
    }

    private fun VectorDrawable.Child.toNode(): IrVectorNode {
        return when (this) {
            is VectorDrawable.Group -> toIrGroup()
            is VectorDrawable.Path -> toIrPath()
            is VectorDrawable.ClipPath -> error("ClipPath should be handled within Group")
        }
    }

    private fun VectorDrawable.Path.toIrPath(): IrVectorNode.IrPath {
        val gradientFill = aaptAttr?.gradient?.toIrFill()
        val colorFill = fillColor?.toIrColor()?.let { IrFill.Color(it) }

        return IrVectorNode.IrPath(
            name = name.orEmpty(),
            fill = gradientFill ?: colorFill,
            fillAlpha = alpha,
            stroke = strokeColor?.toIrColor()?.let { IrStroke.Color(it) },
            strokeAlpha = strokeAlpha?.toFloatOrNull() ?: 1f,
            strokeLineWidth = strokeWidth?.toFloatOrNull() ?: 0f,
            strokeLineCap = strokeLineCap.asStrokeLineCap(),
            strokeLineJoin = strokeLineJoin.asStrokeLineJoin(),
            strokeLineMiter = strokeMiterLimit?.toFloatOrNull() ?: 4f,
            pathFillType = fillType.asFillType(),
            paths = PathParser.parsePathString(pathData),
        )
    }

    private fun VectorDrawable.Group.toIrGroup(): IrVectorNode.IrGroup {
        val clipPathData = children
            .filterIsInstance<VectorDrawable.ClipPath>()
            .flatMap { PathParser.parsePathString(it.pathData) }
            .toMutableList()

        val regularChildren = children
            .filterNot { it is VectorDrawable.ClipPath }
            .map { it.toNode() }
            .toMutableList()

        return IrVectorNode.IrGroup(
            name = name.orEmpty(),
            rotate = rotation ?: 0f,
            pivotX = pivotX ?: 0f,
            pivotY = pivotY ?: 0f,
            scaleX = scaleX ?: 1f,
            scaleY = scaleY ?: 1f,
            translationX = translateX ?: 0f,
            translationY = translateY ?: 0f,
            clipPathData = clipPathData,
            nodes = regularChildren,
        )
    }

    private fun String?.asStrokeLineCap(): IrStrokeLineCap = IrStrokeLineCap.entries.find { it.name.equals(this, ignoreCase = true) } ?: IrStrokeLineCap.Butt

    private fun String?.asStrokeLineJoin(): IrStrokeLineJoin = IrStrokeLineJoin.entries.find { it.name.equals(this, ignoreCase = true) } ?: IrStrokeLineJoin.Miter

    private fun String.asFillType(): IrPathFillType = IrPathFillType.entries.find { it.name.equals(this, ignoreCase = true) } ?: IrPathFillType.NonZero

    private fun String.toIrColor(): IrColor? = AndroidColorParser.parse(this) ?: IrColor(this).takeUnless { it.isTransparent() }

    private fun VectorDrawable.Gradient.toIrFill(): IrFill? {
        return when (type.lowercase()) {
            "linear" -> {
                IrFill.LinearGradient(
                    startX = startX ?: 0f,
                    startY = startY ?: 0f,
                    endX = endX ?: 0f,
                    endY = endY ?: 0f,
                    colorStops = buildColorStops(),
                )
            }
            "radial" -> {
                IrFill.RadialGradient(
                    radius = gradientRadius ?: 0f,
                    centerX = centerX ?: 0f,
                    centerY = centerY ?: 0f,
                    colorStops = buildColorStops(),
                )
            }
            else -> null
        }
    }

    private fun VectorDrawable.Gradient.buildColorStops(): MutableList<IrFill.ColorStop> {
        return if (items.isNotEmpty()) {
            items.mapNotNull { item ->
                item.color.toIrColor()?.let { color ->
                    IrFill.ColorStop(item.offset, color)
                }
            }
        } else {
            buildList {
                startColor?.toIrColor()?.let { color ->
                    add(IrFill.ColorStop(0f, color))
                }
                endColor?.toIrColor()?.let { color ->
                    add(IrFill.ColorStop(1f, color))
                }
            }
        }.toMutableList()
    }
}
