package io.github.composegears.valkyrie.gradle.dsl

import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.reflect.TypeOf

internal inline fun <reified T : Any> ExtensionContainer.create(
    name: String,
    vararg constructionArguments: Any,
): T = create(name, T::class.java, *constructionArguments)

internal inline fun <reified T : Any> ExtensionContainer.getByType(): T = getByType(typeOf<T>())

internal inline fun <reified T : Any> ExtensionContainer.findByType(): T? = findByType(T::class.java)

internal inline fun <reified T> typeOf(): TypeOf<T> = object : TypeOf<T>() {}
