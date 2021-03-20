package tech.blur.armory.presentation.myEvents

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.data.storages.UserStorage
import tech.blur.armory.domain.commands.BookingCommand
import tech.blur.armory.domain.models.Booking
import tech.blur.armory.presentation.common.StateViewModel
import kotlin.properties.Delegates

class MyEventsViewModel(
    private val bookingCommand: BookingCommand,
    userStorage: UserStorage
) : StateViewModel<MyEventsViewState>(MyEventsViewState()) {
    private lateinit var events: List<Booking>
    private var myId: Int by Delegates.notNull()

    init {
        viewModelScope.launch {
            myId = userStorage.getUser().id

            bookingCommand.getMineEvents().onSuccessSuspend {
                events = it
                stateModel = requireStateModel().copy(events = it, myId = myId)
            }.onFailure {
                stateModel = requireStateModel().apply {
                    onError.set(it)
                }
            }
        }
    }

    fun filterEvents(filter: Filter) {
        val newList = when (filter) {
            Filter.ALL -> events
            Filter.MINE -> events.filter {
                it.owner.id == myId
            }
            Filter.INVITES -> events.filter {
                it.owner.id != myId
            }
        }

        stateModel = requireStateModel().copy(events = newList)
    }

    enum class Filter {
        ALL,
        MINE,
        INVITES
    }
}