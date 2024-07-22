package io.github.composegears.valkyrie.generator.imagevector

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.Vector
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.getterFunSpecBuilder
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.ext.setIndent
import io.github.composegears.valkyrie.generator.imagevector.util.addPath
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertyName
import io.github.composegears.valkyrie.generator.imagevector.util.backingPropertySpec
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.imagevector.util.iconPreviewSpecForNestedPack
import io.github.composegears.valkyrie.generator.imagevector.util.imageVectorBuilderSpecs

internal data class ImageVectorSpecConfig(
    val iconName: String,
    val iconPack: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val generatePreview: Boolean,
)

internal class ImageVectorFileSpec(private val config: ImageVectorSpecConfig) {

    fun createFileFor(vector: Vector): ImageVectorSpecOutput {
        val backingProperty = backingPropertySpec(
            name = config.iconName.backingPropertyName(),
            type = ClassNames.ImageVector,
        )

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
                    backingProperty = backingProperty,
                ),
            )
            addProperty(propertySpec = backingProperty)
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
                },
            )
            addStatement("")
            addStatement("return %N!!", backingProperty)
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
                        addStatement(pathNode.asFunctionCall())
                    }
                }
            }
        }
    }
}
