package tech.blur.armory.presentation.settings

import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class SettingsViewState(
    val googleCalendarEnabled: Boolean = false,
    val outlookEnabled: Boolean = false
) : ViewState {
    val loggedOut = Action<Unit>()
}