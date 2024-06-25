package io.github.composegears.valkyrie.generator

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.vector.Vector
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.fileSpecBuilder
import io.github.composegears.valkyrie.generator.ext.getterFunSpecBuilder
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder
import io.github.composegears.valkyrie.generator.ext.removeDeadCode
import io.github.composegears.valkyrie.generator.ext.setIndent
import io.github.composegears.valkyrie.generator.util.addPath
import io.github.composegears.valkyrie.generator.util.backingPropertyName
import io.github.composegears.valkyrie.generator.util.backingPropertySpec
import io.github.composegears.valkyrie.generator.util.iconPreviewSpec
import io.github.composegears.valkyrie.generator.util.imageVectorBuilderSpecs

data class ImageVectorGenerationResult(val sourceCode: String)

data class GeneratorConfig(
    val iconName: String,
    val iconNestedPack: String,
    val iconPackage: String,
    val generatePreview: Boolean,
    val iconPack: ClassName? = null
)

class ImageVectorGenerator(private val config: GeneratorConfig) {

    fun createFileFor(vector: Vector): ImageVectorGenerationResult {
        val backingProperty = backingPropertySpec(
            name = config.iconName.backingPropertyName(),
            type = ClassNames.ImageVector
        )

        val fileSpec = fileSpecBuilder(
            packageName = config.iconPackage,
            fileName = config.iconName
        ) {
            addProperty(propertySpec = iconProperty(vector = vector, backingProperty = backingProperty))
            addProperty(propertySpec = backingProperty)
            apply {
                if (config.generatePreview) {
                    addFunction(
                        funSpec = iconPreviewSpec(
                            iconName = when {
                                config.iconPack != null -> {
                                    MemberName(
                                        enclosingClassName = config.iconPack,
                                        simpleName = config.iconName
                                    )
                                }
                                else -> {
                                    MemberName(
                                        packageName = config.iconPackage,
                                        simpleName = config.iconName
                                    )
                                }
                            }
                        )
                    )
                }
            }
            setIndent()
        }

        return ImageVectorGenerationResult(sourceCode = fileSpec.removeDeadCode())
    }

    private fun iconProperty(vector: Vector, backingProperty: PropertySpec): PropertySpec =
        propertySpecBuilder(name = config.iconName, type = ClassNames.ImageVector) {
            receiver(config.iconPack)
            getter(iconFun(vector = vector, backingProperty = backingProperty))
        }

    private fun iconFun(vector: Vector, backingProperty: PropertySpec): FunSpec {
        return getterFunSpecBuilder {
            addCode(
                buildCodeBlock {
                    beginControlFlow("if (%N != null)", backingProperty)
                    addStatement("return %N!!", backingProperty)
                    endControlFlow()
                }
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
                            }
                        )
                    )
                }
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