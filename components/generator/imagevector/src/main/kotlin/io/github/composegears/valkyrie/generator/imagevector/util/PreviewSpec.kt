package io.github.composegears.valkyrie.generator.imagevector.util

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.buildCodeBlock
import io.github.composegears.valkyrie.generator.ext.funSpecBuilder

internal fun iconPreviewSpecForNestedPack(
    iconName: String,
    iconPackClassName: ClassName,
): FunSpec = funSpecBuilder("${iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addAnnotation(previewAnnotation)
    addAnnotation(composableAnnotation)
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
): FunSpec = funSpecBuilder("${iconName}Preview") {
    addModifiers(KModifier.PRIVATE)
    addAnnotation(previewAnnotation)
    addAnnotation(composableAnnotation)
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

private val composableAnnotation: AnnotationSpec = AnnotationSpec.builder(ClassNames.Composable).build()

private val previewAnnotation = AnnotationSpec
    .builder(ClassNames.Preview)
    .addMember("showBackground = %L", true)
    .build()
