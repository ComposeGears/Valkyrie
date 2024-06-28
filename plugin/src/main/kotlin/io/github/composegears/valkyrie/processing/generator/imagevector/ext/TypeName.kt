package io.github.composegears.valkyrie.processing.generator.imagevector.ext

import com.squareup.kotlinpoet.TypeName

fun TypeName.nullable() = copy(nullable = true)