package io.github.composegears.valkyrie.generator.imagevector.spec

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.vector.Vector
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.getterFunSpecBuilder
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.ext.setIndent
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecOutput
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertyName
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertySpec

internal class BackingPropertySpec(private val config: ImageVectorSpecConfig) {

    fun createAsBackingProperty(vector: Vector): ImageVectorSpecOutput {
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
                    vector = vector,
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
            setIndent()
        }

        return ImageVectorSpecOutput(
            content = fileSpec.removeDeadCode(),
            name = fileSpec.name,
        )
    }

    private fun iconProperty(
        vector: Vector,
        iconPackClassName: ClassName?,
        backingProperty: PropertySpec,
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        getter(iconFun(vector = vector, backingProperty = backingProperty))
    }

    private fun iconFun(vector: Vector, backingProperty: PropertySpec): FunSpec {
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
                    addImageVectorBlock(config = config, vector = vector)
                },
            )
            addStatement("")
            addStatement("return %N!!", backingProperty)
        }
    }
}
