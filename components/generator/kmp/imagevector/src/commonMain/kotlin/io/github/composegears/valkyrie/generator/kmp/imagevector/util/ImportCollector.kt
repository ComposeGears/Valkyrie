package io.github.composegears.valkyrie.generator.kmp.imagevector.util

import io.github.composegears.valkyrie.generator.kmp.imagevector.ImageVectorRenderConfig
import io.github.composegears.valkyrie.generator.kmp.imagevector.render.resolvePackageName
import io.github.composegears.valkyrie.sdk.ir.core.IrFill
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrPathFillType
import io.github.composegears.valkyrie.sdk.ir.core.IrStroke
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineCap
import io.github.composegears.valkyrie.sdk.ir.core.IrStrokeLineJoin
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

internal fun collectImports(config: ImageVectorRenderConfig, vector: IrImageVector): List<String> {
    val usage = UsageFlags.scan(config, vector)
    val imports = mutableListOf<String>()
    if (config.iconPack.isNotEmpty() && config.iconPackPackage != config.resolvePackageName()) {
        imports += "import ${config.iconPackPackage}.${config.iconPack}"
    }

    if (config.generatePreview) {
        imports += "import androidx.compose.foundation.Image"
        imports += "import androidx.compose.foundation.layout.Box"
        imports += "import androidx.compose.foundation.layout.padding"
        imports += "import androidx.compose.runtime.Composable"
        imports += "import androidx.compose.ui.Modifier"
        imports += "import androidx.compose.ui.tooling.preview.Preview"
    }

    with(usage) {
        if (usesOffset && !config.fullQualifiedImports.offset) imports += "import androidx.compose.ui.geometry.Offset"
        if (usesBrush && !config.fullQualifiedImports.brush) imports += "import androidx.compose.ui.graphics.Brush"
        if (usesColor && !config.fullQualifiedImports.color) imports += "import androidx.compose.ui.graphics.Color"
        if (usesPathFillType) imports += "import androidx.compose.ui.graphics.PathFillType"
        if (usesSolidColor) imports += "import androidx.compose.ui.graphics.SolidColor"
        if (usesStrokeCap) imports += "import androidx.compose.ui.graphics.StrokeCap"
        if (usesStrokeJoin) imports += "import androidx.compose.ui.graphics.StrokeJoin"

        imports += "import androidx.compose.ui.graphics.vector.ImageVector"
        if (usesAddPathNodes) imports += "import androidx.compose.ui.graphics.vector.addPathNodes"
        if (usesPathData) imports += "import androidx.compose.ui.graphics.vector.PathData"
        if (usesGroup) imports += "import androidx.compose.ui.graphics.vector.group"
        if (usesPath) imports += "import androidx.compose.ui.graphics.vector.path"
        imports += "import androidx.compose.ui.unit.dp"
    }

    return imports.distinct().sorted()
}

private data class UsageFlags(
    var usesPath: Boolean = false,
    var usesGroup: Boolean = false,
    var usesBrush: Boolean = false,
    var usesOffset: Boolean = false,
    var usesColor: Boolean = false,
    var usesStrokeCap: Boolean = false,
    var usesStrokeJoin: Boolean = false,
    var usesPathFillType: Boolean = false,
    var usesSolidColor: Boolean = false,
    var usesAddPathNodes: Boolean = false,
    var usesPathData: Boolean = false,
) {
    companion object {
        fun scan(config: ImageVectorRenderConfig, vector: IrImageVector): UsageFlags {
            val flags = UsageFlags(usesAddPathNodes = config.usePathDataString)

            fun visit(node: IrVectorNode) {
                when (node) {
                    is IrVectorNode.IrGroup -> {
                        flags.usesGroup = true
                        when {
                            node.clipPathData.isNotEmpty() && config.usePathDataString -> flags.usesAddPathNodes = true
                            node.clipPathData.isNotEmpty() -> flags.usesPathData = true
                        }
                        node.nodes.forEach(::visit)
                    }
                    is IrVectorNode.IrPath -> {
                        when {
                            config.usePathDataString -> flags.usesAddPathNodes = true
                            else -> flags.usesPath = true
                        }
                        when (val fill = node.fill) {
                            is IrFill.Color -> {
                                if (!fill.irColor.isTransparent()) {
                                    flags.usesSolidColor = true
                                    flags.usesColor = true
                                }
                            }
                            is IrFill.LinearGradient -> {
                                flags.usesBrush = true
                                flags.usesOffset = true
                                flags.usesColor = true
                                val hasUnnamedColors = fill.colorStops.any {
                                    it.irColor.toName() == null || !config.useComposeColors
                                }
                                if (hasUnnamedColors) {
                                    flags.usesColor = true
                                }
                            }
                            is IrFill.RadialGradient -> {
                                flags.usesBrush = true
                                flags.usesOffset = true
                                flags.usesColor = true
                            }
                            null -> Unit
                        }

                        val strokeColor = node.stroke as? IrStroke.Color
                        if (strokeColor?.irColor?.isTransparent() == false) {
                            flags.usesSolidColor = true
                            flags.usesColor = true
                        }

                        if (node.strokeLineCap != IrStrokeLineCap.Butt) flags.usesStrokeCap = true
                        if (node.strokeLineJoin != IrStrokeLineJoin.Miter) flags.usesStrokeJoin = true
                        if (node.pathFillType != IrPathFillType.NonZero) flags.usesPathFillType = true
                    }
                }
            }

            vector.nodes.forEach(::visit)
            return flags
        }
    }
}
