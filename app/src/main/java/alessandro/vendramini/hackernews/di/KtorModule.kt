package alessandro.vendramini.hackernews.di

import alessandro.vendramini.hackernews.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val ktorModule = module {
    single { okHttpBuilder() }
}

private fun okHttpBuilder(): HttpClient {
    return HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        engine {
            config {
                followRedirects(true)
            }
            // Custom interceptor for example bearer token
            // addInterceptor(AuthInterceptor())
        }
        defaultRequest {
            // add base url for all request
            url(BuildConfig.BASE_URL)
        }
        install(ContentNegotiation) {
            val converter = KotlinxSerializationConverter(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
            register(
                ContentType.Application.Json,
                converter,
            )
        }
    }
}
