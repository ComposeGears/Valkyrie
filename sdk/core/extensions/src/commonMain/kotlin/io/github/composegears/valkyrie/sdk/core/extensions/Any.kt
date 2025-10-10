package io.github.composegears.valkyrie.sdk.core.extensions

inline fun <reified T : Any> Any?.safeAs(): T? = this as? T

inline fun <reified T : Any> Any?.cast(): T = this as T
