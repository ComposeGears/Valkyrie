package io.github.composegears.valkyrie.util.concurrent

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SingleFlight<K : Any> {
    private val jobs = ConcurrentHashMap<K, Job>()

    fun launchIfAbsent(
        scope: CoroutineScope,
        key: K,
        block: suspend () -> Unit,
    ): Job {
        jobs[key]?.let { return it }
        val job = scope.launch(start = CoroutineStart.LAZY) { block() }
        job.invokeOnCompletion { jobs.remove(key, job) }
        val existing = jobs.putIfAbsent(key, job)
        if (existing != null) {
            job.cancel()
            return existing
        }
        job.start()
        return job
    }

    fun launchReplacing(
        scope: CoroutineScope,
        key: K,
        block: suspend () -> Unit,
    ): Job {
        jobs.remove(key)?.cancel()
        val job = scope.launch { block() }
        job.invokeOnCompletion { jobs.remove(key, job) }
        jobs[key] = job
        return job
    }

    fun cancelAll() {
        jobs.values.forEach { it.cancel() }
        jobs.clear()
    }
}
