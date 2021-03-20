package tech.blur.armory.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.LogOutCommand
import tech.blur.armory.presentation.common.StateViewModel

class SettingsViewModel(
    private val logOutCommand: LogOutCommand
) : StateViewModel<SettingsViewState>(SettingsViewState()) {
    fun logOut() {
        viewModelScope.launch {
            logOutCommand.logOut()
            stateModel = requireStateModel().apply {
                loggedOut.set(Unit)
            }
        }
    }
}