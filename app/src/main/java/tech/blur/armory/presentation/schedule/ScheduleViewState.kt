package tech.blur.armory.presentation.schedule

import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class ScheduleViewState(val items: List<String> = emptyList()) : ViewState {
    val onError = Action<Exception>()
}