package io.github.composegears.valkyrie.ui.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * This is a variant of [MutableStateFlow.update] but using `this` as receiver in [function].
 */
inline fun <T> MutableStateFlow<T>.updateState(function: T.() -> T) {
    update(function)
}
