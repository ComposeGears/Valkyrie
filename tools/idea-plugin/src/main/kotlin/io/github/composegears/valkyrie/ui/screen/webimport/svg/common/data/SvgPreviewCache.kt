package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data

import androidx.collection.LruCache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SvgPreviewCache<T : Any>(
    maxEntries: Int = 256,
) {
    private val guard = Mutex()
    private val keyLocks = mutableMapOf<String, Mutex>()
    private val cache = LruCache<String, T>(maxEntries)

    suspend fun getOrLoad(key: String, loader: suspend () -> T): T {
        val keyLock = guard.withLock {
            cache[key]?.let { return it }
            keyLocks.getOrPut(key) { Mutex() }
        }

        return keyLock.withLock {
            val cached = guard.withLock { cache[key] }
            if (cached != null) return@withLock cached

            try {
                val loaded = loader()
                guard.withLock {
                    cache.put(key, loaded)
                    keyLocks.remove(key)
                    loaded
                }
            } finally {
                guard.withLock {
                    if (keyLocks[key] === keyLock) {
                        keyLocks.remove(key)
                    }
                }
            }
        }
    }
}
