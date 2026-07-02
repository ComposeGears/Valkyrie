package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.resolvePackageName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.ClassNames
import io.github.composegears.valkyrie.sdk.generator.kt.poet.fileSpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.propertySpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.removeExplicitModeCode
import io.github.composegears.valkyrie.sdk.generator.kt.poet.setIndent
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal class LazyPropertySpec(private val config: ImageVectorGeneratorConfig) {

    fun createAsLazyProperty(irVector: IrImageVector): ImageVectorOutput = with(config) {
        val iconPackClassName = resolveIconPackClassName()
        val packageName = resolvePackageName()

        val fileSpec = fileSpecBuilder(
            packageName = packageName,
            fileName = iconName,
        ) {
            addProperty(
                propertySpec = iconProperty(
                    irVector = irVector,
                    iconPackClassName = iconPackClassName,
                ),
            )
            addPreview(
                iconPackClassName = iconPackClassName,
                packageName = packageName,
            )
            setIndent(codeStyle.indentSize)
        }

        return ImageVectorOutput(
            content = when {
                codeStyle.useExplicitMode -> fileSpec.toString()
                else -> fileSpec.removeExplicitModeCode()
            },
            name = fileSpec.name,
        )
    }

    context(config: ImageVectorGeneratorConfig)
    private fun iconProperty(
        irVector: IrImageVector,
        iconPackClassName: ClassName?,
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        val codeBlock = buildCodeBlock {
            addImageVectorBlock(irVector = irVector)
        }

        delegate(
            CodeBlock.builder()
                .beginControlFlow("lazy(%T.NONE)", ClassNames.LazyThreadSafetyMode)
                .add(codeBlock)
                .endControlFlow()
                .build(),
        )
        addSuppressUnusedReceiverAnnotation(iconPackClassName)
    }
}
