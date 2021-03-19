package tech.blur.armory.data.providers

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*

class HttpClientProvider {
    private val client = HttpClient {
//        expectSuccess = false

        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    fun get() = client
}