package tech.blur.armory.presentation.rooms

import androidx.lifecycle.viewModelScope
import com.soywiz.klock.wrapped.value
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.RoomCommand
import tech.blur.armory.domain.models.Event
import tech.blur.armory.domain.models.Room
import tech.blur.armory.domain.models.User
import tech.blur.armory.presentation.common.StateViewModel

class RoomsViewModel(
    private val roomCommand: RoomCommand,
    private val filter: Filter?
) : StateViewModel<RoomsViewState>(RoomsViewState()) {
    init {
        load()
    }

    fun refresh() {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            roomCommand.getAllRooms().mapSuccess {
                requireStateModel().copy(
                    rooms = it.rooms.map { response ->
                        with(response) {
                            Room(
                                id,
                                "$type $id",
                                square,
                                flor,
                                videoconferencing,
                                microphones,
                                led,
                                wifi,
                                seatingCapacity,
                                bookings.map { event ->
                                    Event(
                                        event.id,
                                        event.startTime.value.utc,
                                        event.endTime.value.utc
                                    )
                                },
                                admin?.let {
                                    User(it.id, it.firstName, it.lastName, it.email)
                                }
                            )
                        }
                    }.let { rooms ->
                        if (filter != null) {
                            filterRooms(rooms, filter)
                        } else {
                            rooms
                        }
                    }
                )
            }.onSuccess {
                stateModel = it
            }.onFailure {
                stateModel = requireStateModel().apply {
                    onError.set(it)
                }
            }
        }
    }

    private fun filterRooms(it: List<Room>, filter: Filter) = it.filter { room ->
        val mic = filter.mic?.let {
            if (it) room.mic
            else true
        } ?: true

        val video = filter.video?.let {
            if (it) room.video
            else true
        } ?: true

        val led = filter.led?.let {
            if (it) room.led
            else true
        } ?: true

        val wifi = filter.wifi?.let {
            if (it) room.wifi
            else true
        } ?: true

        val square = filter.square?.let { room.square >= it } ?: true
        val capacity = filter.capacity?.let { room.capacity >= it } ?: true

        mic && video && led && wifi && square && capacity
    }
}