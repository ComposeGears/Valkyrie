package io.github.composegears.valkyrie.generator.kmp.imagevector

import io.github.composegears.valkyrie.generator.core.formatFloat
import io.github.composegears.valkyrie.sdk.ir.core.IrColor
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrPathNode
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode
import io.github.composegears.valkyrie.sdk.ir.core.toPathString

data class SimpleImageVectorGeneratorConfig(
    val packageName: String,
    val useComposeColors: Boolean,
    val addTrailingComma: Boolean,
    val useExplicitMode: Boolean,
    val usePathDataString: Boolean,
    val indentSize: Int,
)

data class SimpleImageVectorSpecOutput(
    val content: String,
    val name: String,
)

object SimpleImageVectorGenerator {

    fun convert(
        vector: IrImageVector,
        iconName: String,
        config: SimpleImageVectorGeneratorConfig,
    ): SimpleImageVectorSpecOutput {
        val imports = ImportFlags.from(vector)
        val body = buildString {
            appendLine("package ${config.packageName}")
            appendLine()
            appendLine("import androidx.compose.ui.graphics.Color")
            if (imports.usesBrush) {
                appendLine("import androidx.compose.ui.graphics.Brush")
            }
            if (imports.usesOffset) {
                appendLine("import androidx.compose.ui.geometry.Offset")
            }
            appendLine("import androidx.compose.ui.graphics.SolidColor")
            appendLine("import androidx.compose.ui.graphics.PathFillType")
            if (imports.usesStrokeCap) {
                appendLine("import androidx.compose.ui.graphics.StrokeCap")
            }
            if (imports.usesStrokeJoin) {
                appendLine("import androidx.compose.ui.graphics.StrokeJoin")
            }
            appendLine("import androidx.compose.ui.graphics.vector.ImageVector")
            appendLine("import androidx.compose.ui.graphics.vector.path")
            if (imports.usesGroup) {
                appendLine("import androidx.compose.ui.graphics.vector.group")
            }
            if (config.usePathDataString || imports.usesPathDataString || imports.usesClipPathData) {
                appendLine("import androidx.compose.ui.graphics.vector.addPathNodes")
            }
            appendLine("import androidx.compose.ui.unit.dp")
            appendLine()

            val explicit = if (config.useExplicitMode) "public " else ""
            appendLine("${explicit}val ${iconName}: ImageVector")
            appendLine("${indent(config, 1)}get() {")
            appendLine("${indent(config, 2)}if (_${iconName} != null) {")
            appendLine("${indent(config, 3)}return _${iconName}!!")
            appendLine("${indent(config, 2)}}")
            appendLine("${indent(config, 2)}_${iconName} = ImageVector.Builder(")

            val builderArgs = buildList {
                add("name = \"${iconName}\"")
                add("defaultWidth = ${vector.defaultWidth.formatFloat().removeSuffix("f")}.dp")
                add("defaultHeight = ${vector.defaultHeight.formatFloat().removeSuffix("f")}.dp")
                add("viewportWidth = ${vector.viewportWidth.formatFloat()}")
                add("viewportHeight = ${vector.viewportHeight.formatFloat()}")
                if (vector.autoMirror) {
                    add("autoMirror = true")
                }
            }

            builderArgs.forEachIndexed { index, arg ->
                val needsComma = index != builderArgs.lastIndex || config.addTrailingComma
                appendLine("${indent(config, 3)}$arg${if (needsComma) "," else ""}")
            }

            appendLine("${indent(config, 2)}).apply {")
            vector.nodes.forEach { node ->
                appendNode(node = node, level = 3, config = config)
            }
            appendLine("${indent(config, 2)}}.build()")
            appendLine()
            appendLine("${indent(config, 2)}return _${iconName}!!")
            appendLine("${indent(config, 1)}}")
            appendLine()
            appendLine("@Suppress(\"ObjectPropertyName\")")
            appendLine("private var _${iconName}: ImageVector? = null")
        }

        return SimpleImageVectorSpecOutput(content = body, name = iconName)
    }

    private fun StringBuilder.appendNode(
        node: IrVectorNode,
        level: Int,
        config: SimpleImageVectorGeneratorConfig,
    ) {
        when (node) {
            is IrVectorNode.IrPath -> appendPath(node, level, config)
            is IrVectorNode.IrGroup -> appendGroup(node, level, config)
        }
    }

    private fun StringBuilder.appendGroup(
        group: IrVectorNode.IrGroup,
        level: Int,
        config: SimpleImageVectorGeneratorConfig,
    ) {
        val params = buildList {
            if (group.name.isNotEmpty()) add("name = \"${group.name.escapeKotlin()}\"")
            if (group.rotate != 0f) add("rotate = ${group.rotate.formatFloat()}")
            if (group.pivotX != 0f) add("pivotX = ${group.pivotX.formatFloat()}")
            if (group.pivotY != 0f) add("pivotY = ${group.pivotY.formatFloat()}")
            if (group.scaleX != 1f) add("scaleX = ${group.scaleX.formatFloat()}")
            if (group.scaleY != 1f) add("scaleY = ${group.scaleY.formatFloat()}")
            if (group.translationX != 0f) add("translationX = ${group.translationX.formatFloat()}")
            if (group.translationY != 0f) add("translationY = ${group.translationY.formatFloat()}")
            if (group.clipPathData.isNotEmpty()) {
                val clipPath = group.clipPathData.toPathString(::formatPathFloat)
                add("clipPathData = addPathNodes(\"${clipPath.escapeKotlin()}\")")
            }
        }

        if (params.isEmpty()) {
            appendLine("${indent(config, level)}group {")
        } else {
            appendLine("${indent(config, level)}group(")
            params.forEachIndexed { index, param ->
                val needsComma = index != params.lastIndex || config.addTrailingComma
                appendLine("${indent(config, level + 1)}$param${if (needsComma) "," else ""}")
            }
            appendLine("${indent(config, level)}) {")
        }

        group.nodes.forEach { child ->
            appendNode(node = child, level = level + 1, config = config)
        }
        appendLine("${indent(config, level)}}")
    }

    private fun StringBuilder.appendPath(
        path: IrVectorNode.IrPath,
        level: Int,
        config: SimpleImageVectorGeneratorConfig,
    ) {
        val params = buildPathParams(path = path, config = config)

        if (config.usePathDataString) {
            appendLine("${indent(config, level)}addPath(")
            params.forEach {
                appendLine("${indent(config, level + 1)}$it,")
            }
            val pathData = path.paths.toPathString(::formatPathFloat)
            val tailComma = if (config.addTrailingComma) "," else ""
            appendLine("${indent(config, level + 1)}pathData = addPathNodes(\"${pathData.escapeKotlin()}\")$tailComma")
            appendLine("${indent(config, level)})")
            return
        }

        if (params.isEmpty()) {
            appendLine("${indent(config, level)}path {")
        } else if (params.size == 1) {
            appendLine("${indent(config, level)}path(${params.first()}) {")
        } else {
            appendLine("${indent(config, level)}path(")
            params.forEachIndexed { index, param ->
                val needsComma = index != params.lastIndex || config.addTrailingComma
                appendLine("${indent(config, level + 1)}$param${if (needsComma) "," else ""}")
            }
            appendLine("${indent(config, level)}) {")
        }

        path.paths.forEach { node ->
            appendLine("${indent(config, level + 1)}${node.toDsl()}")
        }
        appendLine("${indent(config, level)}}")
    }

    private fun buildPathParams(
        path: IrVectorNode.IrPath,
        config: SimpleImageVectorGeneratorConfig,
    ): List<String> {
        return buildList {
            if (path.name.isNotEmpty()) {
                add("name = \"${path.name.escapeKotlin()}\"")
            }

            when (val fill = path.fill) {
                is IrFill.Color -> {
                    if (!fill.irColor.isTransparent()) {
                        add("fill = SolidColor(${fill.irColor.toComposeColor(config.useComposeColors)})")
                    }
                }
                is IrFill.LinearGradient -> {
                    val colorStops = fill.colorStops.joinToString(separator = ", ") {
                        "${it.offset.formatFloat()} to ${it.irColor.toComposeColor(config.useComposeColors)}"
                    }
                    add(
                        "fill = Brush.linearGradient(colorStops = arrayOf($colorStops), " +
                            "start = Offset(${fill.startX.formatFloat()}, ${fill.startY.formatFloat()}), " +
                            "end = Offset(${fill.endX.formatFloat()}, ${fill.endY.formatFloat()}))",
                    )
                }
                is IrFill.RadialGradient -> {
                    val colorStops = fill.colorStops.joinToString(separator = ", ") {
                        "${it.offset.formatFloat()} to ${it.irColor.toComposeColor(config.useComposeColors)}"
                    }
                    add(
                        "fill = Brush.radialGradient(colorStops = arrayOf($colorStops), " +
                            "center = Offset(${fill.centerX.formatFloat()}, ${fill.centerY.formatFloat()}), " +
                            "radius = ${fill.radius.formatFloat()})",
                    )
                }
                null -> Unit
            }

            if (path.fillAlpha != 1f) {
                add("fillAlpha = ${path.fillAlpha.formatFloat()}")
            }

            val stroke = path.stroke
            if (stroke is IrStroke.Color && !stroke.irColor.isTransparent()) {
                add("stroke = SolidColor(${stroke.irColor.toComposeColor(config.useComposeColors)})")
            }
            if (path.strokeAlpha != 1f) {
                add("strokeAlpha = ${path.strokeAlpha.formatFloat()}")
            }
            if (path.strokeLineWidth != 0f) {
                add("strokeLineWidth = ${path.strokeLineWidth.formatFloat()}")
            }
            if (path.strokeLineCap != IrStrokeLineCap.Butt) {
                add("strokeLineCap = StrokeCap.${path.strokeLineCap.name}")
            }
            if (path.strokeLineJoin != IrStrokeLineJoin.Miter) {
                add("strokeLineJoin = StrokeJoin.${path.strokeLineJoin.name}")
            }
            if (path.strokeLineMiter != 4f) {
                add("strokeLineMiter = ${path.strokeLineMiter.formatFloat()}")
            }
            if (path.pathFillType != IrPathFillType.NonZero) {
                add("pathFillType = PathFillType.${path.pathFillType.name}")
            }
        }
    }

    private fun IrColor.toComposeColor(useComposeColors: Boolean): String {
        val named = toName()
        if (useComposeColors && named != null) {
            val alphaValue = alpha.toFloat() / 0xFF
            return if (alphaValue < 1f) {
                "Color.$named.copy(alpha = ${alphaValue.formatFloat()})"
            } else {
                "Color.$named"
            }
        }
        return "Color(${toHexLiteral()})"
    }

    private fun IrPathNode.toDsl(): String {
        return when (this) {
            is IrPathNode.Close -> "close()"
            is IrPathNode.MoveTo -> "moveTo(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.RelativeMoveTo -> "moveToRelative(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.LineTo -> "lineTo(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.RelativeLineTo -> "lineToRelative(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.HorizontalTo -> "horizontalLineTo(${x.formatFloat()})"
            is IrPathNode.RelativeHorizontalTo -> "horizontalLineToRelative(${x.formatFloat()})"
            is IrPathNode.VerticalTo -> "verticalLineTo(${y.formatFloat()})"
            is IrPathNode.RelativeVerticalTo -> "verticalLineToRelative(${y.formatFloat()})"
            is IrPathNode.CurveTo -> {
                "curveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()}, ${x3.formatFloat()}, ${y3.formatFloat()})"
            }
            is IrPathNode.RelativeCurveTo -> {
                "curveToRelative(${dx1.formatFloat()}, ${dy1.formatFloat()}, ${dx2.formatFloat()}, ${dy2.formatFloat()}, ${dx3.formatFloat()}, ${dy3.formatFloat()})"
            }
            is IrPathNode.ReflectiveCurveTo -> {
                "reflectiveCurveTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
            }
            is IrPathNode.RelativeReflectiveCurveTo -> {
                "reflectiveCurveToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
            }
            is IrPathNode.QuadTo -> "quadTo(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
            is IrPathNode.RelativeQuadTo -> {
                "quadToRelative(${x1.formatFloat()}, ${y1.formatFloat()}, ${x2.formatFloat()}, ${y2.formatFloat()})"
            }
            is IrPathNode.ReflectiveQuadTo -> "reflectiveQuadTo(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.RelativeReflectiveQuadTo -> "reflectiveQuadToRelative(${x.formatFloat()}, ${y.formatFloat()})"
            is IrPathNode.ArcTo -> {
                "arcTo(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, ${isMoreThanHalf}, ${isPositiveArc}, ${arcStartX.formatFloat()}, ${arcStartY.formatFloat()})"
            }
            is IrPathNode.RelativeArcTo -> {
                "arcToRelative(${horizontalEllipseRadius.formatFloat()}, ${verticalEllipseRadius.formatFloat()}, ${theta.formatFloat()}, ${isMoreThanHalf}, ${isPositiveArc}, ${arcStartDx.formatFloat()}, ${arcStartDy.formatFloat()})"
            }
        }
    }

    private fun formatPathFloat(value: Float): String = value.formatFloat().removeSuffix("f")

    private fun indent(config: SimpleImageVectorGeneratorConfig, level: Int): String = " ".repeat(config.indentSize * level)

    private fun String.escapeKotlin(): String =
        replace("\\", "\\\\").replace("\"", "\\\"")

    private data class ImportFlags(
        val usesGroup: Boolean,
        val usesBrush: Boolean,
        val usesOffset: Boolean,
        val usesStrokeCap: Boolean,
        val usesStrokeJoin: Boolean,
        val usesPathDataString: Boolean,
        val usesClipPathData: Boolean,
    ) {
        companion object {
            fun from(vector: IrImageVector): ImportFlags {
                var usesGroup = false
                var usesBrush = false
                var usesOffset = false
                var usesStrokeCap = false
                var usesStrokeJoin = false
                var usesPathDataString = false
                var usesClipPathData = false

                fun visit(node: IrVectorNode) {
                    when (node) {
                        is IrVectorNode.IrGroup -> {
                            usesGroup = true
                            if (node.clipPathData.isNotEmpty()) {
                                usesClipPathData = true
                                usesPathDataString = true
                            }
                            node.nodes.forEach(::visit)
                        }
                        is IrVectorNode.IrPath -> {
                            if (node.fill is IrFill.LinearGradient || node.fill is IrFill.RadialGradient) {
                                usesBrush = true
                                usesOffset = true
                            }
                            if (node.strokeLineCap != IrStrokeLineCap.Butt) {
                                usesStrokeCap = true
                            }
                            if (node.strokeLineJoin != IrStrokeLineJoin.Miter) {
                                usesStrokeJoin = true
                            }
                        }
                    }
                }

                vector.nodes.forEach(::visit)

                return ImportFlags(
                    usesGroup = usesGroup,
                    usesBrush = usesBrush,
                    usesOffset = usesOffset,
                    usesStrokeCap = usesStrokeCap,
                    usesStrokeJoin = usesStrokeJoin,
                    usesPathDataString = usesPathDataString,
                    usesClipPathData = usesClipPathData,
                )
            }
        }
    }
}
