package io.github.composegears.valkyrie.sdk.generator.kt.poet

import com.squareup.kotlinpoet.TypeName

public fun TypeName.nullable(): TypeName = copy(nullable = true)
