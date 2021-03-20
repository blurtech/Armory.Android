package tech.blur.armory.data.services.booking

import io.ktor.client.request.*
import tech.blur.armory.common.Result
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.Api
import tech.blur.armory.data.storages.UserStorage

class BookingApi(private val userStorage: UserStorage, httpClientProvider: HttpClientProvider) :
    Api(httpClientProvider.get()) {

    suspend fun getMineEvents(): Result<GetMineBookingsResponse, Exception> {
        return runRequest(GetMineBookingsResponse.serializer()) {
            get {
                url(buildUrl("booking/get-mine"))
                setAuthToken(userStorage)
            }
        }
    }
}