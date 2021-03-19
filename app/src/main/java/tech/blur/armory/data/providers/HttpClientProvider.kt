package tech.blur.armory.data.providers

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

class HttpClientProvider {
    private val client = HttpClient {
//        expectSuccess = false

        install(JsonFeature) {
            acceptContentTypes = listOf(
                ContentType.parse("application/json")
            )
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    fun get() = client
}