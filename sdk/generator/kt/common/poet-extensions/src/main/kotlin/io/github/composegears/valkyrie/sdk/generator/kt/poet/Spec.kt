package io.github.composegears.valkyrie.sdk.generator.kt.poet

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec

public fun FileSpec.Builder.setIndent(indent: Int): FileSpec.Builder = indent(" ".repeat(indent))

public fun FileSpec.removeExplicitModeCode(): String = toString()
    .replace("public ", "")

public inline fun objectBuilder(
    name: String,
    builderAction: TypeSpec.Builder.() -> Unit,
): TypeSpec = TypeSpec.objectBuilder(name)
    .apply(builderAction)
    .build()

public inline fun fileSpecBuilder(
    packageName: String,
    fileName: String,
    builderAction: FileSpec.Builder.() -> Unit,
): FileSpec = FileSpec
    .builder(packageName = packageName, fileName = fileName)
    .addKotlinDefaultImports()
    .apply(builderAction)
    .build()

public inline fun funSpecBuilder(
    name: String,
    builderAction: FunSpec.Builder.() -> Unit,
): FunSpec = FunSpec.builder(name)
    .apply(builderAction)
    .build()

public inline fun getterFunSpecBuilder(
    builderAction: FunSpec.Builder.() -> Unit,
): FunSpec = FunSpec.getterBuilder()
    .apply(builderAction)
    .build()

public inline fun propertySpecBuilder(
    name: String,
    type: TypeName,
    builderAction: PropertySpec.Builder.() -> Unit,
): PropertySpec = PropertySpec
    .builder(name = name, type = type)
    .apply(builderAction)
    .build()
