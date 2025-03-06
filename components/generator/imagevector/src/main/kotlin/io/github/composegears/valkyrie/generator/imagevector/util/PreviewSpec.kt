package io.github.composegears.valkyrie.generator.imagevector.util

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.funSpecBuilder
import io.github.composegears.valkyrie.generator.imagevector.PreviewAnnotationType

internal fun iconPreviewSpecForNestedPack(
    iconName: String,
    iconPackClassName: ClassName,
    previewAnnotationType: PreviewAnnotationType,
): FunSpec = funSpecBuilder("${iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addPreviewAnnotation(previewAnnotationType)
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
                iconPackClassName.nestedClass(iconName),
            )
            endControlFlow()
        },
    )
}

internal fun iconPreviewSpec(
    iconPackage: String,
    iconName: String,
    previewAnnotationType: PreviewAnnotationType,
): FunSpec = funSpecBuilder("${iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addPreviewAnnotation(previewAnnotationType)
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
                    simpleName = iconName,
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
