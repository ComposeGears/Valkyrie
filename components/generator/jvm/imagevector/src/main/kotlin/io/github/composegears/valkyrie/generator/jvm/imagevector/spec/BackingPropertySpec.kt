package io.github.composegears.valkyrie.generator.jvm.imagevector.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.jvm.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.jvm.ext.getterFunSpecBuilder
import io.github.composegears.valkyrie.generator.jvm.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.jvm.ext.removeExplicitModeCode
import io.github.composegears.valkyrie.generator.jvm.ext.setIndent
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecOutput
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.ClassNames
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.backingPropertyName
import io.github.composegears.valkyrie.generator.jvm.imagevector.util.backingPropertySpec
import io.github.composegears.valkyrie.ir.IrImageVector

internal class BackingPropertySpec(private val config: ImageVectorSpecConfig) {

    fun createAsBackingProperty(irVector: IrImageVector): ImageVectorSpecOutput = with(config) {
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
            setIndent(indentSize)
        }

        return ImageVectorSpecOutput(
            content = when {
                useExplicitMode -> fileSpec.toString()
                else -> fileSpec.removeExplicitModeCode()
            },
            name = fileSpec.name,
        )
    }

    context(config: ImageVectorSpecConfig)
    private fun iconProperty(
        irVector: IrImageVector,
        iconPackClassName: ClassName?,
        backingProperty: PropertySpec,
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        getter(iconFun(irVector = irVector, backingProperty = backingProperty))
    }

    context(config: ImageVectorSpecConfig)
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
