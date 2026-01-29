package io.github.composegears.valkyrie.jewel.platform.picker

interface Picker<T> {

    suspend fun launch(): T
}
