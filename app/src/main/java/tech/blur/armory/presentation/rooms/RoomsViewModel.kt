package tech.blur.armory.presentation.rooms

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.RoomCommand
import tech.blur.armory.presentation.common.StateViewModel

class RoomsViewModel(
    private val roomCommand: RoomCommand
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
                            RoomsViewState.Room(
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
                                    RoomsViewState.Event(
                                        event.id,
                                        event.startTime.value,
                                        event.endTime.value
                                    )
                                }
                            )
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

    private fun Int.toBoolean(): Boolean {
        return this != 0
    }
}