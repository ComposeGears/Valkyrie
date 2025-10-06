package io.github.composegears.valkyrie.util

import com.composegears.tiamat.navigation.SavedState
import io.github.composegears.valkyrie.extensions.safeAs

inline fun <reified T : Any> SavedState.getOrNull(key: String): T? = get(key).safeAs<T>()
