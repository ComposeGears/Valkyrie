package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorGeneratorConfig
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.ImageVectorOutput
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.common.resolvePackageName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.ClassNames
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.backingPropertyName
import io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util.backingPropertySpec
import io.github.composegears.valkyrie.sdk.generator.kt.poet.fileSpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.getterFunSpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.propertySpecBuilder
import io.github.composegears.valkyrie.sdk.generator.kt.poet.removeExplicitModeCode
import io.github.composegears.valkyrie.sdk.generator.kt.poet.setIndent
import io.github.composegears.valkyrie.sdk.ir.core.IrImageVector

internal class BackingPropertySpec(private val config: ImageVectorGeneratorConfig) {

    fun createAsBackingProperty(irVector: IrImageVector): ImageVectorOutput = with(config) {
        val backingProperty = backingPropertySpec(
            name = iconName.backingPropertyName(),
            type = ClassNames.ImageVector,
        )

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
                    backingProperty = backingProperty,
                ),
            )
            addProperty(propertySpec = backingProperty)
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
        backingProperty: PropertySpec,
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        getter(iconFun(irVector = irVector, backingProperty = backingProperty))
        addSuppressUnusedReceiverAnnotation(iconPackClassName)
    }

    context(config: ImageVectorGeneratorConfig)
    private fun iconFun(irVector: IrImageVector, backingProperty: PropertySpec): FunSpec {
        return getterFunSpecBuilder {
            addCode(
                buildCodeBlock {
                    beginControlFlow("if (%N != null)", backingProperty)
                    addStatement("return %N!!", backingProperty)
                    endControlFlow()
                },
            )
            addCode(
                buildCodeBlock {
                    addCode("%N = ", backingProperty)
                    addImageVectorBlock(irVector = irVector)
                },
            )
            addStatement("")
            addStatement("return %N!!", backingProperty)
        }
    }
}
