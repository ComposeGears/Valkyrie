package io.github.composegears.valkyrie.ui.screen.webimport.svg.common.data

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SvgPreviewCacheTest {

    @Test
    fun `cache reuses loaded preview by key`() = runTest {
        val cache = SvgPreviewCache<String>()
        var loadCount = 0

        val first = cache.getOrLoad("heroicons:academic-cap") {
            loadCount++
            "preview"
        }
        val second = cache.getOrLoad("heroicons:academic-cap") {
            loadCount++
            "other"
        }

        assertThat(first).isEqualTo("preview")
        assertThat(second).isEqualTo("preview")
        assertThat(loadCount).isEqualTo(1)
    }

    @Test
    fun `cache evicts least recently used preview when full`() = runTest {
        val cache = SvgPreviewCache<String>(maxEntries = 2)

        cache.getOrLoad("first") { "one" }
        cache.getOrLoad("second") { "two" }
        cache.getOrLoad("first") { "one-again" }
        cache.getOrLoad("third") { "three" }

        val second = cache.getOrLoad("second") { "two-again" }

        assertThat(second).isEqualTo("two-again")
    }

    @Test
    fun `cache deduplicates concurrent loads for same key`() = runTest {
        val cache = SvgPreviewCache<String>()
        var loadCount = 0

        val previews = List(8) {
            async(Dispatchers.Default) {
                cache.getOrLoad("same") {
                    loadCount++
                    delay(10.milliseconds)
                    "preview"
                }
            }
        }.awaitAll()

        assertThat(previews.distinct()).isEqualTo(listOf("preview"))
        assertThat(loadCount).isEqualTo(1)
    }

    @Test
    fun `cache removes key lock when loader fails`() = runTest {
        val cache = SvgPreviewCache<String>()
        var loadCount = 0

        assertFailure {
            cache.getOrLoad("failing") {
                loadCount++
                error("failed")
            }
        }

        val preview = cache.getOrLoad("failing") {
            loadCount++
            "preview"
        }

        assertThat(preview).isEqualTo("preview")
        assertThat(loadCount).isEqualTo(2)
    }
}
