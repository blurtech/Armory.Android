package tech.blur.armory.domain.models

import com.soywiz.klock.DateTime

data class Booking(
        val id: Int,
        val startTime: DateTime,
        val endTime: DateTime,
        val meetingRoomBooking: Room,
        val owner: User,
        val userList: List<User>
)