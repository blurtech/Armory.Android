package tech.blur.armory.presentation.rooms

import com.soywiz.klock.DateTime
import tech.blur.armory.domain.models.Room
import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class RoomsViewState(
    val rooms: List<Room> = emptyList()
): ViewState {

    val onError = Action<Exception>()
}