package io.github.composegears.valkyrie.ui.screen.webimport.lucide.di

import com.composegears.leviathan.Leviathan
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.data.LucideRepository
import io.github.composegears.valkyrie.ui.screen.webimport.lucide.domain.LucideUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.json.Json

object LucideModule : Leviathan() {
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

    private val lucideRepository by instanceOf {
        LucideRepository(
            httpClient = inject(httpClient),
            json = inject(json),
        )
    }

    val lucideUseCase by instanceOf {
        LucideUseCase(
            repository = inject(lucideRepository),
        )
    }
}
