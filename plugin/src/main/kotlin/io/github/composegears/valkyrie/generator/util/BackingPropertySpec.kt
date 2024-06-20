package io.github.composegears.valkyrie.generator.util

import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeName
import io.github.composegears.valkyrie.generator.ext.nullable
import io.github.composegears.valkyrie.generator.ext.propertySpecBuilder

fun String.backingPropertyName() = "_$this"

internal fun backingPropertySpec(
    name: String,
    type: TypeName
) = propertySpecBuilder(name = name, type = type.nullable()) {
    mutable()
    addModifiers(KModifier.PRIVATE)
    initializer("null")
}