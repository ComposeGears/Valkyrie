package io.github.composegears.valkyrie.gradle.dsl

import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property

internal inline fun <reified T : Any> ObjectFactory.property(): Property<T> = property(T::class.java)
internal inline fun <reified T : Any> ObjectFactory.listProperty(): ListProperty<T> = listProperty(T::class.java)

internal inline fun <reified T : Any> ObjectFactory.newInstance(vararg parameters: Any): T {
    return newInstance(T::class.java, *parameters)
}
