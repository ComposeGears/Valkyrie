package io.github.composegears.valkyrie.generator.jvm.imagevector.util

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import io.github.composegears.valkyrie.generator.jvm.ext.nullable
import io.github.composegears.valkyrie.generator.jvm.ext.propertySpecBuilder

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
