package tech.blur.armory.data.services

import io.ktor.client.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import tech.blur.armory.common.Result

abstract class Api(private val client: HttpClient) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    protected suspend fun <T> runRequest(
        serializer: KSerializer<T>,
        builder: suspend HttpClient.() -> HttpResponse,
    ): Result<T, Exception> {
        return try {
            val response = builder(client)

            val body = response.content.readUTF8Line()
            Result.success(json.decodeFromString(serializer, body!!))
//            return if (response.status.isSuccess()) {
//
//            } else {
//                val error = try {
//                    response.convertToApiException()
//                } catch (e: Exception) {
//                    UnknownException(e)
//                }
//
//                Result.failure(error)
//            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    protected fun buildUrl(path: String) = "$URL$path"

    companion object {
        private const val URL = "https://armory-backend.herokuapp.com/"
    }
}