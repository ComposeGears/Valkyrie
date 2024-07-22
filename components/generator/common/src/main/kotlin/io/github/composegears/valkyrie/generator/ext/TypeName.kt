package io.github.composegears.valkyrie.generator.ext

import com.squareup.kotlinpoet.TypeName

fun TypeName.nullable() = copy(nullable = true)
