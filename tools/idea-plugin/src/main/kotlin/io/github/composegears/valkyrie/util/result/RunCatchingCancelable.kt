package io.github.composegears.valkyrie.util.result

import kotlin.coroutines.cancellation.CancellationException

inline fun <R> runCatchingCancellable(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
