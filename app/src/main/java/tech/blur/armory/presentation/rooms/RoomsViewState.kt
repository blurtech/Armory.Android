package tech.blur.armory.presentation.rooms

import com.soywiz.klock.DateTime
import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class RoomsViewState(
    val rooms: List<Room> = emptyList()
): ViewState {

    val onError = Action<Exception>()

    data class Room(
        val id: Int,
        val name: String,
        val square: Double,
        val flor: Int,
        val video: Boolean,
        val mic: Boolean,
        val led: Boolean,
        val wifi: Boolean,
        val capacity: Int,
        val nearEvents: List<Event>
    )

    data class Event(
        val id: Int,
        val startTime: DateTime,
        val endTime: DateTime
    )
}