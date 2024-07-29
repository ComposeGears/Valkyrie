package io.github.composegears.valkyrie.generator.imagevector.spec

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.vector.Vector
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.ext.setIndent
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.imagevector.ImageVectorSpecOutput

internal class LazyDelegatePropertySpec(private val config: ImageVectorSpecConfig) {

    fun createAsLazyDelegateProperty(vector: Vector): ImageVectorSpecOutput {
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
                ),
            )
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
    ): PropertySpec = propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
        receiver(iconPackClassName)
        val codeBlock = buildCodeBlock {
            addImageVectorBlock(config = config, vector = vector)
        }

        delegate(
            CodeBlock.builder()
                .beginControlFlow("lazy(%T.NONE)", ClassNames.LazyThreadSafetyMode)
                .add(codeBlock)
                .endControlFlow()
                .build(),
        )
    }
}
