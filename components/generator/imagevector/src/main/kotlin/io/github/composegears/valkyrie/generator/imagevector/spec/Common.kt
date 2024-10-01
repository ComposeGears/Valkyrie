package io.github.composegears.valkyrie.generator.imagevector.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.util.MemberNames
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
            ClassName(iconPackage, iconPack)
        } else {
            ClassName(iconPackage, iconPack).nestedClass(iconNestedPack)
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
                irVector.nodes.forEach { node -> addVectorNode(node) }
            },
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
                )
                else -> iconPreviewSpec(
                    iconPackage = packageName,
                    iconName = config.iconName,
                )
            },
        )
    }
}

private fun CodeBlock.Builder.addVectorNode(irVectorNode: IrVectorNode) {
    when (irVectorNode) {
        is IrVectorNode.IrGroup -> {
            beginControlFlow("%M", MemberNames.Group)
            irVectorNode.paths.forEach { path ->
                addVectorNode(path)
            }
            endControlFlow()
        }
        is IrVectorNode.IrPath -> {
            addPath(irVectorNode) {
                irVectorNode.paths.forEach { pathNode ->
                    // based on https://github.com/square/kotlinpoet/pull/1860#issuecomment-1986825382
                    addStatement("%L", pathNode.asStatement().replace(' ', '·'))
                }
            }
        }
    }
}
