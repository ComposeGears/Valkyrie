package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.jvm.ext.funSpecBuilder
import io.github.composegears.valkyrie.generator.jvm.imagevector.ImageVectorSpecConfig
import io.github.composegears.valkyrie.generator.jvm.imagevector.PreviewAnnotationType

context(config: ImageVectorSpecConfig)
internal fun iconPreviewSpecForNestedPack(
    iconPackClassName: ClassName,
): FunSpec = funSpecBuilder("${config.iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addPreviewAnnotation(config.previewAnnotationType)
    addComposableAnnotation()
    addCode(
        codeBlock = buildCodeBlock {
            beginControlFlow(
                controlFlow = "%M(modifier = %M.%M(12.%M))",
                MemberNames.Box,
                MemberNames.Modifier,
                MemberNames.Padding,
                MemberNames.Dp,
            )
            addStatement(
                format = "%M(imageVector = %T, contentDescription = null)",
                MemberNames.Image,
                iconPackClassName.nestedClass(config.iconName),
            )
            endControlFlow()
        },
    )
}

context(config: ImageVectorSpecConfig)
internal fun iconPreviewSpec(
    iconPackage: String,
): FunSpec = funSpecBuilder("${config.iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addPreviewAnnotation(config.previewAnnotationType)
    addComposableAnnotation()
    addCode(
        codeBlock = buildCodeBlock {
            beginControlFlow(
                controlFlow = "%M(modifier = %M.%M(12.%M))",
                MemberNames.Box,
                MemberNames.Modifier,
                MemberNames.Padding,
                MemberNames.Dp,
            )
            addStatement(
                format = "%M(imageVector = %M, contentDescription = null)",
                MemberNames.Image,
                MemberName(
                    packageName = iconPackage,
                    simpleName = config.iconName,
                ),
            )
            endControlFlow()
        },
    )
}

private fun FunSpec.Builder.addPreviewAnnotation(annotationType: PreviewAnnotationType) {
    val previewClassName = when (annotationType) {
        PreviewAnnotationType.AndroidX -> ClassNames.AndroidXPreview
        PreviewAnnotationType.Jetbrains -> ClassNames.JetbrainsPreview
    }
    addAnnotation(AnnotationSpec.builder(previewClassName).build())
}

private fun FunSpec.Builder.addComposableAnnotation() {
    addAnnotation(AnnotationSpec.builder(ClassNames.Composable).build())
}
