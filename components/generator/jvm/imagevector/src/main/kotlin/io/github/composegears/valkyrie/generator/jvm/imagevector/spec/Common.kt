package io.github.composegears.valkyrie.generator.jvm.imagevector.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import io.github.composegears.valkyrie.generator.core.asStatement
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.addGroup
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.addPath
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.addPathData
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.imageVectorBuilderSpecs
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

internal fun ImageVectorSpecConfig.resolvePackageName(): String = when {
    iconNestedPack.isEmpty() -> iconPackage
    else -> {
        if (useFlatPackage) {
            iconPackage
        } else {
            "$iconPackage.${iconNestedPack.lowercase()}"
        }
    }
}

internal fun ImageVectorSpecConfig.resolveIconPackClassName() = when {
    iconPack.isEmpty() -> null
    else -> {
        if (iconNestedPack.isEmpty()) {
            ClassName(iconPackPackage, iconPack)
        } else {
            ClassName(iconPackPackage, iconPack).nestedClass(iconNestedPack)
        }
    }
}

context(config: ImageVectorSpecConfig)
internal fun CodeBlock.Builder.addImageVectorBlock(irVector: IrImageVector) {
    add(
        imageVectorBuilderSpecs(
            iconName = when {
                config.iconNestedPack.isEmpty() -> config.iconName
                else -> "${config.iconNestedPack}.${config.iconName}"
            },
            irVector = irVector,
            path = {
                irVector.nodes.forEach { node ->
                    addVectorNode(irVectorNode = node)
                }
            },
        ),
    )
}

context(config: ImageVectorSpecConfig)
internal fun FileSpec.Builder.addPreview(
    iconPackClassName: ClassName?,
    packageName: String,
) {
    if (config.generatePreview) {
        addFunction(
            funSpec = when {
                iconPackClassName != null -> iconPreviewSpecForNestedPack(iconPackClassName = iconPackClassName)
                else -> iconPreviewSpec(iconPackage = packageName)
            },
        )
    }
}

context(config: ImageVectorSpecConfig)
private fun CodeBlock.Builder.addVectorNode(irVectorNode: IrVectorNode) {
    when (irVectorNode) {
        is IrVectorNode.IrGroup -> addGroup(
            path = irVectorNode,
            groupBody = {
                irVectorNode.nodes.forEach { node ->
                    addVectorNode(irVectorNode = node)
                }
            },
        )
        is IrVectorNode.IrPath -> {
            if (config.usePathDataString) {
                addPathData(path = irVectorNode)
            } else {
                addPath(
                    path = irVectorNode,
                    pathBody = {
                        irVectorNode.paths.forEach { pathNode ->
                            // based on https://github.com/square/kotlinpoet/pull/1860#issuecomment-1986825382
                            addStatement("%L", pathNode.asStatement().replace(' ', '·'))
                        }
                    },
                )
            }
        }
    }
}
