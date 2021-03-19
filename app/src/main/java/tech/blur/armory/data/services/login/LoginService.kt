package tech.blur.armory.data.services.login

import tech.blur.armory.common.Result
import tech.blur.armory.domain.models.User

class LoginService(private val authApi: AuthApi) {
    suspend fun login(email: String, password: String): Result<User, Exception> {
        return authApi.login(LoginRequest(email, password))
            .mapSuccess { (id, email, name, surname, token) ->
                User(id, email, name, surname, token)
            }
    }

    suspend fun register(name: String, surname: String, email: String, password: String) =
        authApi.register(RegisterRequest(name, surname, email, password))
            .mapSuccess { (id, email, name, surname, token) ->
                User(id, email, name, surname, token)
            }
}