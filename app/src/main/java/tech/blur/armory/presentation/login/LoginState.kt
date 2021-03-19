package tech.blur.armory.presentation.login

import tech.blur.armory.presentation.common.Action
import tech.blur.armory.presentation.common.ViewState

class LoginViewState: ViewState {
    val onLoginComlete =  Action<Unit>()
    val onError =  Action<Exception>()
}