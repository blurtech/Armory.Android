package tech.blur.armory.data.services.room

import io.ktor.client.request.*
import tech.blur.armory.common.Result
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.Api
import tech.blur.armory.data.storages.UserStorage

class RoomApi(private val userStorage: UserStorage, httpClientProvider: HttpClientProvider) :
    Api(httpClientProvider.get()) {
    suspend fun getAllRooms(): Result<GetAllMeetingRoomResponse, Exception> {
        return runRequest(GetAllMeetingRoomResponse.serializer()) {
            get {
                url(buildUrl("meeting-room/get-all"))
                setAuthToken(userStorage)
            }
        }
    }
}