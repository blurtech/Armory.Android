package tech.blur.armory.presentation.login

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.LoginCommand
import tech.blur.armory.presentation.common.StateViewModel

class LoginViewModel(
    private val loginCommand: LoginCommand
) : StateViewModel<LoginViewState>(LoginViewState()) {

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            loginCommand.login(email, password).onSuccess {
                stateModel = requireStateModel().apply {
                    onLoginComlete.set(Unit)
                }
            }.onFailure {
                stateModel = requireStateModel().apply {
                    onError.set(it)
                }
            }
        }
    }
}