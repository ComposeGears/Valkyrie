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
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.generator.imagevector.util.imageVectorBuilderSpecs

internal class LazyDelegatePropertySpec(
    private val config: ImageVectorSpecConfig,
) {

    fun createAsLazyDelegateProperty(vector: Vector): ImageVectorSpecOutput {
        val iconPackClassName = when {
            config.iconPack.isEmpty() -> null
            else -> {
                if (config.iconNestedPack.isEmpty()) {
                    ClassName(
                        config.iconPackage,
                        config.iconPack,
                    )
                } else {
                    ClassName(
                        config.iconPackage,
                        config.iconPack,
                    ).nestedClass(config.iconNestedPack)
                }
            }
        }

        val packageName = when {
            config.iconNestedPack.isEmpty() -> config.iconPackage
            else -> "${config.iconPackage}.${config.iconNestedPack.lowercase()}"
        }

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
            apply {
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

        delegate(
            CodeBlock.builder()
                .beginControlFlow("lazy(%T.NONE)", ClassNames.LazyThreadSafetyMode)
                .add(codeBlock)
                .endControlFlow()
                .build(),
        )
    }
}
