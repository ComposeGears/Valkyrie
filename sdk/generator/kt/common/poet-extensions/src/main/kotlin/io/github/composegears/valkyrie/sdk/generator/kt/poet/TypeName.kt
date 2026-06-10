package io.github.composegears.valkyrie.sdk.generator.kt.poet

import com.squareup.kotlinpoet.TypeName

fun TypeName.nullable() = copy(nullable = true)
