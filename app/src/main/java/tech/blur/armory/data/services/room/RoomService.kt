package tech.blur.armory.data.services.room

class RoomService(private val roomApi: RoomApi) {
    suspend fun getAllRooms() = roomApi.getAllRooms()
}