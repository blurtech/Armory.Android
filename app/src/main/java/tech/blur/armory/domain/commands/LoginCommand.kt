package tech.blur.armory.domain.commands

import tech.blur.armory.common.Result
import tech.blur.armory.data.services.login.LoginService
import tech.blur.armory.data.storages.UserStorage
import tech.blur.armory.domain.models.MyUser

class LoginCommand(
    private val loginService: LoginService,
    private val userStorage: UserStorage
) {
    suspend fun login(email: String, password: String): Result<MyUser, Exception> {
        val userResult = loginService.login(email, password)

        userResult.onSuccessSuspend {
            userStorage.insert(it)
        }

        return userResult
    }

    suspend fun register(
        name: String,
        surname: String,
        email: String,
        password: String
    ): Result<MyUser, Exception> {
        val userResult = loginService.register(name, surname, email, password)

        userResult.onSuccessSuspend {
            userStorage.insert(it)
        }

        return userResult
    }

    suspend fun isLoggedIn(): Boolean {
        return userStorage.isUserExists()
    }
}