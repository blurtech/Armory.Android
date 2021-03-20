package tech.blur.armory.domain.models

sealed class RoomStatus {
    object Free : RoomStatus()
    object Busy : RoomStatus()
    data class NearBusy(val time: String) : RoomStatus()
}
