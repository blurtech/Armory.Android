package tech.blur.armory.data.services.login

import tech.blur.armory.common.Result
import tech.blur.armory.domain.models.MyUser

class LoginService(private val authApi: AuthApi) {
    suspend fun login(email: String, password: String): Result<MyUser, Exception> {
        return authApi.login(LoginRequest(email, password))
            .mapSuccess { (id, email, name, surname, token) ->
                MyUser(id, email, name, surname, token)
            }
    }

    suspend fun register(name: String, surname: String, email: String, password: String) =
        authApi.register(RegisterRequest(name, surname, email, password))
            .mapSuccess { (id, email, name, surname, token) ->
                MyUser(id, email, name, surname, token)
            }
}