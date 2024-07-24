package io.github.composegears.valkyrie.generator.imagevector.util

import androidx.compose.material.icons.generator.ClassNames
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import io.github.composegears.valkyrie.generator.ext.nullable
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder

internal fun String.backingPropertyName() = "_$this"

internal fun backingPropertySpec(
    name: String,
    type: TypeName,
) = propertySpecBuilder(name = name, type = type.nullable()) {
    addAnnotation(suppressNamingAnnotation)
    mutable()
    addModifiers(KModifier.PRIVATE)
    initializer("null")
}

private val suppressNamingAnnotation = AnnotationSpec.builder(ClassNames.Suppress)
    .addMember("%S", "ObjectPropertyName")
    .build()
