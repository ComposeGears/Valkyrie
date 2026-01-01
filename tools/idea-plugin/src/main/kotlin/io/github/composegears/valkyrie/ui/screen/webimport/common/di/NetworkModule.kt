package io.github.composegears.valkyrie.ui.screen.webimport.common.di

import com.composegears.leviathan.Leviathan
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
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
        }
    }
}
