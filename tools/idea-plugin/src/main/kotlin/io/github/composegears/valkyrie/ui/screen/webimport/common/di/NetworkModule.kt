package io.github.composegears.valkyrie.ui.screen.webimport.common.di

import com.composegears.leviathan.Leviathan
import com.intellij.openapi.application.PathManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.json.Json

object NetworkModule : Leviathan() {

    val json by instanceOf(keepAlive = true) {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    val httpClient by instanceOf {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30.seconds.inWholeMilliseconds
            }
            install(ContentNegotiation) {
                json(inject(json))
            }
            install(HttpCache) {
                val cacheDir = Paths.get(PathManager.getSystemPath()).resolve("valkyrie")
                val cacheFile = Files.createDirectories(cacheDir).toFile()
                publicStorage(FileStorage(cacheFile))
            }
        }
    }
}
