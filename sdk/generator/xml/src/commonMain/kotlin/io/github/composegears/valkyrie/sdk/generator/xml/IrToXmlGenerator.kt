package io.github.composegears.valkyrie.sdk.generator.xml

import io.github.composegears.valkyrie.ir.IrFill
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrPathFillType
import io.github.composegears.valkyrie.ir.IrStroke
import io.github.composegears.valkyrie.ir.IrVectorNode
import io.github.composegears.valkyrie.ir.util.toPathString
import io.github.composegears.valkyrie.sdk.core.xml.VectorDrawable
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
        return VectorDrawable.Group(
            name = name.ifEmpty { null },
            pivotX = pivotX.takeIf { it != 0f },
            pivotY = pivotY.takeIf { it != 0f },
            translateX = translationX.takeIf { it != 0f },
            translateY = translationY.takeIf { it != 0f },
            scaleX = scaleX.takeIf { it != 1f },
            scaleY = scaleY.takeIf { it != 1f },
            rotation = rotate.takeIf { it != 0f },
            children = nodes.map { it.toVdChild() },
        )
    }

    private fun IrVectorNode.IrPath.toVdPath(): VectorDrawable.Path {
        val fillColorStr = when (val f = fill) {
            is IrFill.Color -> f.irColor.toHexColor()
            else -> null
        }
        val strokeColorStr = when (val s = stroke) {
            is IrStroke.Color -> s.irColor.toHexColor()
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
