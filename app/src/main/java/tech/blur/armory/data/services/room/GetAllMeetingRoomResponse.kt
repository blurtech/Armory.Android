@file:UseSerializers(DateTimeTzSerializer::class)

package tech.blur.armory.data.services.room

import com.soywiz.klock.wrapped.WDateTimeTz
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import tech.blur.armory.common.DateTimeTzSerializer

@Serializable
data class GetAllMeetingRoomResponse(
    val rooms: List<MeetingRoom>
) {
    @Serializable
    data class MeetingRoom(
        val id: Int,
        val flor: Int,
        val type: String,
        val square: Double,
        val videoconferencing: Boolean,
        val microphones: Boolean,
        val wifi: Boolean,
        val led: Boolean,
        val seatingCapacity: Int,
        val admin: User?,
        val bookings: List<Booking>
    )

    @Serializable
    data class Booking(
        val id: Int,
        val startTime: WDateTimeTz,
        val endTime: WDateTimeTz,
    )

    @Serializable
    data class User(
        val id: Int,
        val firstName: String,
        val lastName: String,
        val email: String
    )
}