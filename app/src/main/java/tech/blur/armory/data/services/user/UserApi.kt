package tech.blur.armory.data.services.user

import io.ktor.client.request.*
import tech.blur.armory.common.Result
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.Api
import tech.blur.armory.data.storages.UserStorage
import tech.blur.armory.domain.models.User

class UserApi(private val userStorage: UserStorage, httpClientProvider: HttpClientProvider) :
    Api(httpClientProvider.get()) {

    suspend fun getUserByEmail(email: String): Result<User, Exception> {
        return runRequest(User.serializer()) {
            get {
                url(buildUrl("users/find-by-email?email=$email"))
                setAuthToken(userStorage)
            }
        }
    }
}