package tech.blur.armory.presentation.registration

import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

data class RegistrationViewState(
    val isFieldsValid: Boolean = true,
    val isEmailValid: Boolean = true,
    val isEmailAlreadyTaken: Boolean = true
): ViewState {
    val onRegistrationSucceed = Action<Unit>()
    val onError =  Action<Exception>()
}