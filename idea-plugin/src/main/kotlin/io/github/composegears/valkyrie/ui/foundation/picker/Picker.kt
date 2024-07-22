package io.github.composegears.valkyrie.ui.foundation.picker

interface Picker<T> {

    suspend fun launch(): T
}