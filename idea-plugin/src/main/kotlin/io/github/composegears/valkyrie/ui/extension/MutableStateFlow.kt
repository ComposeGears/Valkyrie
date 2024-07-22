package io.github.composegears.valkyrie.ui.extension

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

fun <T> MutableStateFlow<T>.updateState(reduce: T.() -> T) {
    update(reduce)
}
