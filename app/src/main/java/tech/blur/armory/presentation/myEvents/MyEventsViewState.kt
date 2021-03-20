package tech.blur.armory.presentation.myEvents

import tech.blur.armory.domain.models.Booking
import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class MyEventsViewState(
    val events: List<Booking> = emptyList(),
    val myId: Int = -1
) : ViewState {
    val onError = Action<Exception>()
}