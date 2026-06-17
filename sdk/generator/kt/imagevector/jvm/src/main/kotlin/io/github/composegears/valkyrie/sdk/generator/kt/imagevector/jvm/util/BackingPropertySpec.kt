package io.github.composegears.valkyrie.sdk.generator.kt.imagevector.jvm.util

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import io.github.composegears.valkyrie.sdk.generator.kt.poet.nullable
import io.github.composegears.valkyrie.sdk.generator.kt.poet.propertySpecBuilder

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
