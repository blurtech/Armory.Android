package tech.blur.armory.data.services.booking

import com.soywiz.klock.wrapped.value
import tech.blur.armory.domain.models.Booking
import tech.blur.armory.domain.models.Event
import tech.blur.armory.domain.models.Room
import tech.blur.armory.domain.models.User

class BookingService(private val bookingApi: BookingApi) {
    suspend fun getMineEvents() = bookingApi.getMineEvents().mapSuccess {
        it.bookings.map { response ->
            Booking(
                response.id,
                response.name,
                response.startTime.value.utc,
                response.endTime.value.utc,
                response.meetingRoomBooking.let { room ->
                    Room(
                        id = room.id,
                        flor = room.flor,
                        name = room.type,
                        square = room.square,
                        video = room.videoconferencing,
                        mic = room.microphones,
                        wifi = room.wifi,
                        led = room.led,
                        capacity = room.seatingCapacity,
                        admin = room.admin?.let {
                            User(it.id, it.firstName, it.lastName, it.email)
                        },
                        nearEvents = room.bookings.map {
                            Event(
                                it.id,
                                it.startTime.value.utc,
                                it.endTime.value.utc
                            )
                        },
                    )
                },
                response.owner.let {
                    User(it.id, it.firstName, it.lastName, it.email)
                },
                response.userList.map {
                    User(it.id, it.firstName, it.lastName, it.email)
                }
            )
        }
    }
}