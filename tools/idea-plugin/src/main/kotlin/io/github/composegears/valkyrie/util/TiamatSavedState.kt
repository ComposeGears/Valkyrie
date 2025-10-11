package io.github.composegears.valkyrie.util

import com.composegears.tiamat.SavedState
import io.github.composegears.valkyrie.sdk.core.extensions.safeAs

inline fun <reified T : Any> SavedState.getOrNull(key: String): T? = get(key).safeAs<T>()
