package io.github.composegears.valkyrie.generator.imagevector.spec

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.getterFunSpecBuilder
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.ext.removeExplicitModeCode
import io.github.composegears.valkyrie.generator.ext.setIndent
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecOutput
import io.github.composegears.valkyrie.generator.imagevector.util.ClassNames
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertyName
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertySpec
import io.github.composegears.valkyrie.ir.IrImageVector

internal class BackingPropertySpec(private val config: ImageVectorSpecConfig) {

    fun createAsBackingProperty(irVector: IrImageVector): ImageVectorSpecOutput {
        val backingProperty = backingPropertySpec(
            name = config.iconName.backingPropertyName(),
            type = ClassNames.ImageVector,
        )

        val iconPackClassName = config.resolveIconPackClassName()
        val packageName = config.resolvePackageName()

        val fileSpec = fileSpecBuilder(
            packageName = packageName,
            fileName = config.iconName,
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
                config = config,
                iconPackClassName = iconPackClassName,
                packageName = packageName,
            )
            setIndent(config.indentSize)
        }

        return ImageVectorSpecOutput(
            content = when {
                config.useExplicitMode -> fileSpec.toString()
                else -> fileSpec.removeExplicitModeCode()
            },
            name = fileSpec.name,
        )
    }

    private fun iconProperty(
        irVector: IrImageVector,
        iconPackClassName: ClassName?,
        backingProperty: PropertySpec,
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        getter(iconFun(irVector = irVector, backingProperty = backingProperty))
    }

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
                    addImageVectorBlock(config = config, irVector = irVector)
                },
            )
            addStatement("")
            addStatement("return %N!!", backingProperty)
        }
    }
}
