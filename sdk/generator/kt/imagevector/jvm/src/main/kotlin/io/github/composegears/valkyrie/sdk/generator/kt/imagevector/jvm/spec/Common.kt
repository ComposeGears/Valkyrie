package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import io.github.composegears.valkyrie.generator.kt.common.ir.asStatement
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.resolveIconBuilderName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.ClassNames
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.addGroup
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.addPath
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.addPathData
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.iconPreviewSpec
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.imageVectorBuilderSpecs
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector
import io.github.composegears.valkyrie.sdk.ir.core.IrVectorNode

context(config: ImageVectorGeneratorConfig)
internal fun CodeBlock.Builder.addImageVectorBlock(irVector: IrImageVector) {
    add(
        imageVectorBuilderSpecs(
            iconName = config.resolveIconBuilderName(),
            irVector = irVector,
            path = {
                irVector.nodes.forEach { node ->
                    addVectorNode(irVectorNode = node)
                }
            },
        ),
    )
}

context(config: ImageVectorGeneratorConfig)
internal fun FileSpec.Builder.addPreview(
    iconPackClassName: ClassName?,
    packageName: String,
) {
    if (config.imageVector.generatePreview) {
        addFunction(
            funSpec = when {
                iconPackClassName != null -> iconPreviewSpecForNestedPack(iconPackClassName = iconPackClassName)
                else -> iconPreviewSpec(iconPackage = packageName)
            },
        )
    }
}

context(config: ImageVectorGeneratorConfig)
internal fun PropertySpec.Builder.addSuppressUnusedReceiverAnnotation(iconPackClassName: ClassName?) {
    if (iconPackClassName != null && config.imageVector.suppressUnusedReceiverWarning) {
        addAnnotation(
            AnnotationSpec.builder(ClassNames.Suppress)
                .addMember("%S", "UnusedReceiverParameter")
                .build(),
        )
    }
}

context(config: ImageVectorGeneratorConfig)
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
            if (config.imageVector.usePathDataString) {
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
