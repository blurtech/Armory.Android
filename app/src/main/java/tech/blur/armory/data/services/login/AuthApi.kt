package tech.blur.armory.data.services.login

import io.ktor.client.request.*
import io.ktor.http.*
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.Api

class AuthApi(httpClientProvider: HttpClientProvider) : Api(httpClientProvider.get()) {
    suspend fun login(loginRequest: LoginRequest) =
        runRequest(LoginResponse.serializer()) {
            post {
                url(buildUrl("authorization"))
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                body = loginRequest
            }
        }

    suspend fun register(registerRequest: RegisterRequest) =
        runRequest(LoginResponse.serializer()) {
            post {
                url(buildUrl("registration"))
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                body = registerRequest
            }
        }
}
