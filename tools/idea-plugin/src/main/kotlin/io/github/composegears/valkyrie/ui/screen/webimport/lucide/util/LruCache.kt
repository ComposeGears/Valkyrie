package io.github.composegears.valkyrie.ui.screen.webimport.lucide.util

/**
 * Simple LRU (Least Recently Used) cache implementation with automatic eviction.
 * When capacity is exceeded, the least recently accessed entry is automatically removed.
 *
 * Thread-safe operations are available via [ThreadSafeLruCache].
 */
class LruCache<K, V>(private val maxSize: Int) {
    private val cache = LinkedHashMap<K, V>(maxSize, 0.75f, true)

    operator fun get(key: K): V? = cache[key]

    operator fun set(key: K, value: V) {
        cache[key] = value
        if (cache.size > maxSize) {
            val oldestKey = cache.keys.first()
            cache.remove(oldestKey)
        }
    }
}

/**
 * Thread-safe wrapper around [LruCache] using synchronization.
 * Use this when the cache needs to be accessed from multiple threads.
 */
class ThreadSafeLruCache<K, V>(private val maxSize: Int) {
    private val cache = LinkedHashMap<K, V>(maxSize, 0.75f, true)

    operator fun get(key: K): V? = synchronized(this) {
        cache[key]
    }

    operator fun set(key: K, value: V) = synchronized(this) {
        cache[key] = value
        while (cache.size > maxSize) {
            val oldestKey = cache.keys.firstOrNull() ?: break
            cache.remove(oldestKey)
        }
    }
}
