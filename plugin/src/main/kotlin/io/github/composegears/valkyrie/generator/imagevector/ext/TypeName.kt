package io.github.composegears.valkyrie.generator.imagevector.ext

import com.squareup.kotlinpoet.TypeName

fun TypeName.nullable() = copy(nullable = true)