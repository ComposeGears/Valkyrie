package io.github.composegears.valkyrie.processing.generator.imagevector.util

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import io.github.composegears.valkyrie.processing.generator.imagevector.ext.nullable
import io.github.composegears.valkyrie.processing.generator.imagevector.ext.propertySpecBuilder

fun String.backingPropertyName() = "_$this"

internal fun backingPropertySpec(
    name: String,
    type: TypeName
) = propertySpecBuilder(name = name, type = type.nullable()) {
    mutable()
    addModifiers(KModifier.PRIVATE)
    initializer("null")
}