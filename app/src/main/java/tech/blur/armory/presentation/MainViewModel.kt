package tech.blur.armory.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.LoginCommand
import tech.blur.armory.presentation.common.StateViewModel

class MainViewModel(
    private val loginCommand: LoginCommand
): StateViewModel<MainViewState>() {
    init {
        viewModelScope.launch {
            stateModel = MainViewState(loginCommand.isLoggedIn())
        }
    }
}