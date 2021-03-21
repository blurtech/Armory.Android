package tech.blur.armory.presentation.schedule

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.data.services.booking.BookingApi
import tech.blur.armory.presentation.common.StateViewModel

class ScheduleViewModel(private val bookingApi: BookingApi, private val roomId: Int) :
    StateViewModel<ScheduleViewState>(ScheduleViewState()) {
    fun getBooking(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            bookingApi.get(roomId, year, month, day).onSuccess {
                val list = if (it.isEmpty()) listOf("") else it.map {
                    "${it.startTime.format("HH:mm")} - ${it.endTime.format("HH:mm")}"
                }

                stateModel = requireStateModel().copy(items = list)
            }.onFailure {
                stateModel = requireStateModel().copy().apply {
                    onError.set(it)
                }
            }
        }
    }
}