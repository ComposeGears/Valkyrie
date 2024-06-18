package io.github.composegears.valkyrie.generator

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.MemberNames
import androidx.compose.material.icons.generator.PackageNames
import androidx.compose.material.icons.generator.vector.Vector
import androidx.compose.material.icons.generator.vector.VectorNode
import com.squareup.kotlinpoet.*
import io.github.composegears.valkyrie.generator.ext.*
import io.github.composegears.valkyrie.generator.util.addPath
import io.github.composegears.valkyrie.generator.util.backingPropertyName
import io.github.composegears.valkyrie.generator.util.backingPropertySpec
import io.github.composegears.valkyrie.generator.util.imageVectorBuilderSpecs

data class ImageVectorGenerationResult(
    val sourceCode: String
)

class ImageVectorGenerator(
    private val iconName: String,
    private val iconGroupPackage: String,
    private val vector: Vector,
    private val generatePreview: Boolean
) {

    fun createFileSpec(receiverClass: ClassName): ImageVectorGenerationResult {
        val backingProperty = backingPropertySpec(
            name = iconName.backingPropertyName(),
            type = ClassNames.ImageVector
        )

        val fileSpec = fileSpecBuilder(packageName = iconGroupPackage, fileName = iconName) {
            addProperty(propertySpec = iconProperty(receiverClass, backingProperty))
            addProperty(propertySpec = backingProperty)
            apply {
                if (generatePreview) {
                    addFunction(
                        funSpec = iconPreviewSpec(
                            MemberName(
                                enclosingClassName = receiverClass,
                                simpleName = iconName
                            )
                        )
                    )
                }
            }
            setIndent()
        }

        return ImageVectorGenerationResult(sourceCode = fileSpec.removeDeadCode())
    }

    private fun iconProperty(
        receiverClass: ClassName,
        backingProperty: PropertySpec
    ): PropertySpec =
        propertySpecBuilder(name = iconName, type = ClassNames.ImageVector) {
            receiver(receiverClass)
            getter(iconFun(backingProperty))
        }

    private fun iconFun(backingProperty: PropertySpec): FunSpec {
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
                            iconName = iconName,
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

    private fun iconPreviewSpec(iconName: MemberName): FunSpec {
        val previewAnnotation = AnnotationSpec.builder(ClassNames.Preview).build()
        val composableAnnotation = AnnotationSpec.builder(ClassNames.Composable).build()
        val box = MemberName(PackageNames.LayoutPackage.packageName, "Box")
        val modifier = MemberName(PackageNames.UiPackage.packageName, "Modifier")
        val padding = MemberName(PackageNames.LayoutPackage.packageName, "padding")
        val paddingValue = MemberNames.Dp
        val composeImage = MemberName(PackageNames.FoundationPackage.packageName, "Image")

        return funSpecBuilder("Preview") {
            addModifiers(KModifier.PRIVATE)
            addAnnotation(previewAnnotation)
            addAnnotation(composableAnnotation)
            addCode(
                codeBlock = buildCodeBlock {
                    beginControlFlow("%M(modifier = %M.%M(12.%M))", box, modifier, padding, paddingValue)
                    addStatement("%M(imageVector = %M, contentDescription = \"\")", composeImage, iconName)
                    endControlFlow()
                }
            )
        }
    }
}