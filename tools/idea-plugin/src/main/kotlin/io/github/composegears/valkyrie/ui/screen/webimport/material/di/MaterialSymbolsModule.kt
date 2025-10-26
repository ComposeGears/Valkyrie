package io.github.composegears.valkyrie.ui.screen.webimport.material.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.config.MaterialSymbolsConfigRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.data.font.MaterialFontRepository
import io.github.composegears.valkyrie.ui.screen.webimport.material.domain.MaterialSymbolsConfigUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.json.Json

object MaterialSymbolsModule : Leviathan() {
    private val json by instanceOf {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    private val httpClient by instanceOf {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30.seconds.inWholeMilliseconds
            }
            install(ContentNegotiation) {
                json(inject(json))
            }
        }
    }
    private val materialSymbolsConfigRepository by instanceOf {
        MaterialSymbolsConfigRepository(
            httpClient = inject(httpClient),
            json = inject(json),
        )
    }
    private val materialFontRepository by instanceOf {
        MaterialFontRepository(httpClient = inject(httpClient))
    }
    val materialSymbolsConfigUseCase by instanceOf {
        MaterialSymbolsConfigUseCase(
            configRepository = inject(materialSymbolsConfigRepository),
            fontRepository = inject(materialFontRepository),
        )
    }
}
