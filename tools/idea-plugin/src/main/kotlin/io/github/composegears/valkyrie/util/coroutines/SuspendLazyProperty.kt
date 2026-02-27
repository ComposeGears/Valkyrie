package io.github.composegears.valkyrie.util.coroutines

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendLazyProperty<T>(
    private val initializer: suspend () -> T,
) {
    private val mutex = Mutex()

    @Volatile
    private var value: Any? = Uninitialized

    suspend operator fun invoke(): T = getValue()

    @Suppress("UNCHECKED_CAST")
    private suspend fun getValue(): T {
        val v1 = value
        if (v1 !== Uninitialized) return v1 as T

        return mutex.withLock {
            val v2 = value
            if (v2 !== Uninitialized) return v2 as T

            initializer().also { value = it }
        }
    }

    private object Uninitialized
}

fun <T> suspendLazy(initializer: suspend () -> T): SuspendLazyProperty<T> = SuspendLazyProperty(initializer)
