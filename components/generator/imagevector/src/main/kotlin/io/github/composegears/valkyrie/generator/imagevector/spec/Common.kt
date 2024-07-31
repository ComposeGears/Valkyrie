package io.github.composegears.valkyrie.generator.imagevector.spec

import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.Vector
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.util.addPath
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.generator.imagevector.util.imageVectorBuilderSpecs

internal fun ImageVectorSpecConfig.resolvePackageName(): String = when {
    iconNestedPack.isEmpty() -> iconPackage
    else -> "$iconPackage.${iconNestedPack.lowercase()}"
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
    vector: Vector,
) {
    add(
        imageVectorBuilderSpecs(
            iconName = when {
                config.iconNestedPack.isEmpty() -> config.iconName
                else -> "${config.iconNestedPack}.${config.iconName}"
            },
            vector = vector,
            path = {
                vector.nodes.forEach { node -> addVectorNode(node) }
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

private fun CodeBlock.Builder.addVectorNode(vectorNode: VectorNode) {
    when (vectorNode) {
        is VectorNode.Group -> {
            beginControlFlow("%M", MemberNames.Group)
            vectorNode.paths.forEach { path ->
                addVectorNode(path)
            }
            endControlFlow()
        }
        is VectorNode.Path -> {
            addPath(vectorNode) {
                vectorNode.nodes.forEach { pathNode ->
                    // based on https://github.com/square/kotlinpoet/pull/1860#issuecomment-1986825382
                    addStatement("%L", pathNode.asFunctionCall().replace(' ', '·'))
                }
            }
        }
    }
}
