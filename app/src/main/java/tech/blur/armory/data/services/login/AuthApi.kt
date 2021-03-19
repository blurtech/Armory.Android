package tech.blur.armory.data.services.login

import io.ktor.client.request.*
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.Api

class AuthApi(httpClientProvider: HttpClientProvider) : Api(httpClientProvider.get()) {
    suspend fun login(loginRequest: LoginRequest) =
        runRequest(LoginResponse.serializer()) {
            post {
                url(buildUrl("authorization"))
                body = loginRequest
            }
        }

    suspend fun register(registerRequest: RegisterRequest) =
        runRequest(LoginResponse.serializer()) {
            post {
                url(buildUrl("authorization"))
                body = registerRequest
            }
        }
}
