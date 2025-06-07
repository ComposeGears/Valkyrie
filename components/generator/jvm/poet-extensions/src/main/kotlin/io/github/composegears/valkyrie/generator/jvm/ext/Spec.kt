package io.github.composegears.valkyrie.generator.jvm.ext

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName

fun FileSpec.Builder.setIndent(indent: Int) = indent(" ".repeat(indent))

fun FileSpec.removeExplicitModeCode(): String = toString()
    .replace("public ", "")

inline fun fileSpecBuilder(
    packageName: String,
    fileName: String,
    builderAction: FileSpec.Builder.() -> Unit,
) = FileSpec
    .builder(packageName = packageName, fileName = fileName)
    .addKotlinDefaultImports()
    .apply(builderAction)
    .build()

inline fun funSpecBuilder(
    name: String,
    builderAction: FunSpec.Builder.() -> Unit,
) = FunSpec.builder(name)
    .apply(builderAction)
    .build()

inline fun getterFunSpecBuilder(
    builderAction: FunSpec.Builder.() -> Unit,
) = FunSpec.getterBuilder()
    .apply(builderAction)
    .build()

inline fun propertySpecBuilder(
    name: String,
    type: TypeName,
    builderAction: PropertySpec.Builder.() -> Unit,
) = PropertySpec
    .builder(name = name, type = type)
    .apply(builderAction)
    .build()
