package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.ir.core.toPathString
import kotlin.math.round

object IrToXmlGenerator {

    fun generate(imageVector: IrImageVector): String {
        return XmlSerializer.serialize(imageVector.toVectorDrawable())
    }

    private fun IrImageVector.toVectorDrawable(): VectorDrawable {
        return VectorDrawable(
            name = name.ifEmpty { null },
            widthInDp = formatDpValue(defaultWidth),
            heightInDp = formatDpValue(defaultHeight),
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            tint = null,
            autoMirrored = autoMirror,
            children = nodes.map { it.toVdChild() },
        )
    }

    private fun IrVectorNode.toVdChild(): VectorDrawable.Child = when (this) {
        is IrVectorNode.IrGroup -> toVdGroup()
        is IrVectorNode.IrPath -> toVdPath()
    }

    private fun IrVectorNode.IrGroup.toVdGroup(): VectorDrawable.Group {
        val regularChildren = nodes.map { it.toVdChild() }

        // Generate clip-path element if present
        // ClipPath must be the first child in the group to properly define the clipping region
        val allChildren = if (clipPathData.isNotEmpty()) {
            val clipPath = VectorDrawable.ClipPath(
                name = null,
                pathData = clipPathData.toPathString(::formatFloatValue),
            )
            listOf(clipPath) + regularChildren
        } else {
            regularChildren
        }

        return VectorDrawable.Group(
            name = name.ifEmpty { null },
            pivotX = pivotX.takeIf { it != 0f },
            pivotY = pivotY.takeIf { it != 0f },
            translateX = translationX.takeIf { it != 0f },
            translateY = translationY.takeIf { it != 0f },
            scaleX = scaleX.takeIf { it != 1f },
            scaleY = scaleY.takeIf { it != 1f },
            rotation = rotate.takeIf { it != 0f },
            children = allChildren,
        )
    }

    private fun IrVectorNode.IrPath.toVdPath(): VectorDrawable.Path {
        val fillColorStr = when (val fill = fill) {
            is IrFill.Color -> fill.irColor.toHexColor()
            else -> null
        }
        val strokeColorStr = when (val stroke = stroke) {
            is IrStroke.Color -> stroke.irColor.toHexColor()
            else -> null
        }

        return VectorDrawable.Path(
            name = name.ifEmpty { null },
            fillType = when (pathFillType) {
                IrPathFillType.NonZero -> "nonZero"
                IrPathFillType.EvenOdd -> "evenOdd"
            },
            fillColor = fillColorStr,
            pathData = paths.toPathString(::formatFloatValue),
            alpha = fillAlpha,
            strokeWidth = strokeLineWidth.takeIf { it != 0f }?.let { formatFloatValue(it) },
            strokeLineCap = if (strokeLineCap.name == "Butt") null else strokeLineCap.name.lowercase(),
            strokeLineJoin = if (strokeLineJoin.name == "Miter") null else strokeLineJoin.name.lowercase(),
            strokeColor = strokeColorStr,
            strokeAlpha = strokeAlpha.takeIf { it != 1f }?.let { formatFloatValue(it) },
            strokeMiterLimit = strokeLineMiter.takeIf { it != 4f }?.let { formatFloatValue(it) },
            aaptAttr = fill?.toAaptAttr(),
        )
    }

    private fun IrFill.toAaptAttr(): VectorDrawable.AaptAttr? {
        return when (this) {
            is IrFill.LinearGradient -> VectorDrawable.AaptAttr(
                name = "android:fillColor",
                gradient = toVectorDrawableGradient(),
            )
            is IrFill.RadialGradient -> VectorDrawable.AaptAttr(
                name = "android:fillColor",
                gradient = toVectorDrawableGradient(),
            )
            else -> null
        }
    }

    private fun IrFill.LinearGradient.toVectorDrawableGradient(): VectorDrawable.Gradient {
        // Use startColor/endColor for simple 2-color gradients (0.0 to 1.0)
        val useSimpleFormat = colorStops.size == 2 &&
            colorStops.first().offset == 0f &&
            colorStops.last().offset == 1f

        return VectorDrawable.Gradient(
            type = "linear",
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            centerX = null,
            centerY = null,
            gradientRadius = null,
            startColor = colorStops.first().irColor.toHexColor().takeIf { useSimpleFormat },
            endColor = colorStops.last().irColor.toHexColor().takeIf { useSimpleFormat },
            items = if (useSimpleFormat) {
                emptyList()
            } else {
                colorStops.map { colorStop ->
                    VectorDrawable.GradientItem(
                        color = colorStop.irColor.toHexColor(),
                        offset = colorStop.offset,
                    )
                }
            },
        )
    }

    private fun IrFill.RadialGradient.toVectorDrawableGradient(): VectorDrawable.Gradient {
        // Use startColor/endColor for simple 2-color gradients (0.0 to 1.0)
        val useSimpleFormat = colorStops.size == 2 &&
            colorStops.first().offset == 0f &&
            colorStops.last().offset == 1f

        return VectorDrawable.Gradient(
            type = "radial",
            startX = null,
            startY = null,
            endX = null,
            endY = null,
            centerX = centerX,
            centerY = centerY,
            gradientRadius = radius,
            startColor = colorStops.first().irColor.toHexColor().takeIf { useSimpleFormat },
            endColor = colorStops.last().irColor.toHexColor().takeIf { useSimpleFormat },
            items = if (useSimpleFormat) {
                emptyList()
            } else {
                colorStops.map { colorStop ->
                    VectorDrawable.GradientItem(
                        color = colorStop.irColor.toHexColor(),
                        offset = colorStop.offset,
                    )
                }
            },
        )
    }

    private fun formatDpValue(value: Float): String {
        return if (value.isFinite()) "${formatFloatValue(value)}dp" else "0dp"
    }

    private fun formatFloatValue(v: Float): String {
        if (!v.isFinite()) return "0"
        val scale = 100000f
        val scaled = round(v * scale) / scale
        val raw = scaled.toString()
        return raw.trimEnd('0').trimEnd('.')
    }
}
