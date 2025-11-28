package io.github.composegears.valkyrie.gradle.dsl

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property

internal inline fun <reified T : Any> ObjectFactory.property(): Property<T> = property(T::class.java)

internal inline fun <reified T : Any> ObjectFactory.newInstance(vararg parameters: Any): T = newInstance(T::class.java, *parameters)
