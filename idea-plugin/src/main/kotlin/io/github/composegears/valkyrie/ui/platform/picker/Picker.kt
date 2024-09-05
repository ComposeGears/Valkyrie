package io.github.composegears.valkyrie.ui.platform.picker

interface Picker<T> {

    suspend fun launch(): T
}
