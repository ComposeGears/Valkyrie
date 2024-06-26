package io.github.composegears.valkyrie.generator.util

import androidx.compose.material.icons.generator.ClassNames
import androidx.compose.material.icons.generator.MemberNames
import com.squareup.kotlinpoet.*
import io.github.composegears.valkyrie.generator.ext.funSpecBuilder

fun iconPreviewSpec(iconName: MemberName): FunSpec {
    return funSpecBuilder("Preview") {
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
                    MemberNames.Dp
                )
                addStatement(
                    format = "%M(imageVector = %M, contentDescription = null)",
                    MemberNames.Image,
                    iconName
                )
                endControlFlow()
            }
        )
    }
}

private val composableAnnotation: AnnotationSpec = AnnotationSpec.builder(ClassNames.Composable).build()

private val previewAnnotation = AnnotationSpec
    .builder(ClassNames.Preview)
    .addMember("showBackground = %L", true)
    .build()
