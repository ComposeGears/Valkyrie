package io.github.composegears.valkyrie.generator.imagevector.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.util.addGroup
import io.github.composegears.valkyrie.generator.imagevector.util.addPath
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.generator.imagevector.util.imageVectorBuilderSpecs
import io.github.composegears.valkyrie.ir.IrImageVector
import io.github.composegears.valkyrie.ir.IrVectorNode

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

internal fun CodeBlock.Builder.addImageVectorBlock(
    config: ImageVectorSpecConfig,
    irVector: IrImageVector,
) {
    add(
        imageVectorBuilderSpecs(
            iconName = when {
                config.iconNestedPack.isEmpty() -> config.iconName
                else -> "${config.iconNestedPack}.${config.iconName}"
            },
            irVector = irVector,
            path = {
                irVector.nodes.forEach { node ->
                    addVectorNode(
                        irVectorNode = node,
                        addTrailingComma = config.addTrailingComma,
                        useComposeColors = config.useComposeColors,
                    )
                }
            },
            addTrailingComma = config.addTrailingComma,
        ),
    )
}

internal fun FileSpec.Builder.addPreview(
    config: ImageVectorSpecConfig,
    iconPackClassName: ClassName?,
    packageName: String,
) {
    if (config.generatePreview) {
        addFunction(
            funSpec = when {
                iconPackClassName != null -> iconPreviewSpecForNestedPack(
                    iconPackClassName = iconPackClassName,
                    iconName = config.iconName,
                    previewAnnotationType = config.previewAnnotationType,
                )
                else -> iconPreviewSpec(
                    iconPackage = packageName,
                    iconName = config.iconName,
                    previewAnnotationType = config.previewAnnotationType,
                )
            },
        )
    }
}

private fun CodeBlock.Builder.addVectorNode(
    irVectorNode: IrVectorNode,
    addTrailingComma: Boolean,
    useComposeColors: Boolean,
) {
    when (irVectorNode) {
        is IrVectorNode.IrGroup -> addGroup(
            path = irVectorNode,
            addTrailingComma = addTrailingComma,
            groupBody = {
                irVectorNode.paths.forEach { path ->
                    addVectorNode(
                        irVectorNode = path,
                        addTrailingComma = addTrailingComma,
                        useComposeColors = useComposeColors,
                    )
                }
            },
        )
        is IrVectorNode.IrPath -> addPath(
            path = irVectorNode,
            addTrailingComma = addTrailingComma,
            useComposeColor = useComposeColors,
            pathBody = {
                irVectorNode.paths.forEach { pathNode ->
                    // based on https://github.com/square/kotlinpoet/pull/1860#issuecomment-1986825382
                    addStatement("%L", pathNode.asStatement().replace(' ', '·'))
                }
            },
        )
    }
}
