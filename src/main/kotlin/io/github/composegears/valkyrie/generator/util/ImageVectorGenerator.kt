package io.github.composegears.valkyrie.generator.util

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.VectorAssetGenerator
import androidx.compose.material.icons.generator.addRecursively
import androidx.compose.material.icons.generator.util.withBackingProperty
import androidx.compose.material.icons.generator.vector.Vector
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.buildCodeBlock

data class ImageVectorGenerationResult(
    val sourceCode: String
)

class ImageVectorGenerator(
    private val iconName: String,
    private val iconGroupPackage: String,
    private val vector: Vector,
    private val generatePreview: Boolean
) {
    private val vectorAssetGenerator = VectorAssetGenerator(
        iconName = iconName,
        iconGroupPackage = iconGroupPackage,
        vector = vector,
        generatePreview = generatePreview
    )

    fun createFileSpec(receiverClass: ClassName): ImageVectorGenerationResult {
        val result = vectorAssetGenerator.createFileSpec(
            receiverClass,
            iconProperty = {
                iconProperty(receiverClass, it)
            }
        )

        return ImageVectorGenerationResult(
            sourceCode = result.sourceGeneration
                .toString()
                .replace("public ", "")
        )
    }

    private fun iconProperty(
        receiverClass: ClassName,
        backingProperty: PropertySpec
    ): PropertySpec = PropertySpec.builder(name = iconName, type = ClassNames.ImageVector)
        .receiver(receiverClass)
        .getter(iconFun(backingProperty))
        .build()

    private fun iconFun(backingProperty: PropertySpec): FunSpec {
        return FunSpec.getterBuilder()
            .withBackingProperty(backingProperty) {
                addCode(
                    buildCodeBlock {
                        addCode("%N = ", backingProperty)
                        add(
                            imageVectorBuilderSpecs(
                                iconName = iconName,
                                vector = vector,
                                path = {
                                    vector.nodes.forEach { node -> addRecursively(node) }
                                }
                            )
                        )
                    }
                )
            }
            .build()
    }
}