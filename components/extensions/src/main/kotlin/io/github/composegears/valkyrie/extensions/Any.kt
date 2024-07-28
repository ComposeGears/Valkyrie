package io.github.composegears.valkyrie.extensions

inline fun <reified T : Any> Any?.castOrNull(): T? = this as? T
