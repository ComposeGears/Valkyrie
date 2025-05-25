package io.github.composegears.valkyrie.generator.jvm.ext

import com.squareup.kotlinpoet.TypeName

fun TypeName.nullable() = copy(nullable = true)
