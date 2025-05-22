package io.github.composegears.valkyrie.extensions

inline fun <reified T : Any> Any?.safeAs(): T? = this as? T

inline fun <reified T : Any> Any?.cast(): T = this as T
