@file:Suppress("NOTHING_TO_INLINE")

package io.github.composegears.valkyrie.ui.extension

inline fun String?.or(default: String): String = this ?: default
