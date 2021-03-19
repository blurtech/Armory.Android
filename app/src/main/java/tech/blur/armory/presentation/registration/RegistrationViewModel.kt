package tech.blur.armory.presentation.registration

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tech.blur.armory.domain.commands.LoginCommand
import tech.blur.armory.presentation.common.StateViewModel

class RegistrationViewModel(
    private val loginCommand: LoginCommand
) : StateViewModel<RegistrationViewState>(RegistrationViewState()) {
    val nameFlow = MutableStateFlow("")
    val surnameFlow = MutableStateFlow("")
    val emailFlow = MutableStateFlow("")
    val passwordFlow = MutableStateFlow("")

    init {
        nameFlow
            .drop(1)
            .debounce(300)
            .onEach {
                updateValid()
            }.launchIn(viewModelScope)

        surnameFlow
            .drop(1)
            .debounce(300)
            .onEach {
                updateValid()
            }.launchIn(viewModelScope)

        emailFlow
            .drop(1)
            .debounce(300)
            .onEach {
                if (EmailFormatValidator.validateEmailFormat(it) != EmailFormatValidator.EmailValidationResult.VALID) {
                    stateModel = requireStateModel().copy(isEmailValid = false)
                } else {
                    //todo check email

                    stateModel = requireStateModel().copy(isEmailValid = true)
                    updateValid()
                }
            }.launchIn(viewModelScope)

        passwordFlow
            .drop(1)
            .debounce(300)
            .onEach {
                updateValid()
            }.launchIn(viewModelScope)
    }

    fun register() {
        viewModelScope.launch {
            loginCommand.register(
                name = nameFlow.value,
                surname = surnameFlow.value,
                email = emailFlow.value,
                password = passwordFlow.value
            ).onSuccess {
                stateModel = requireStateModel().apply {
                    onRegistrationSucceed.set(Unit)
                }
            }.onFailure {
                stateModel = requireStateModel().apply {
                    onError.set(it)
                }
            }
        }
    }

    private fun updateValid() {
        if (
            nameFlow.value.isNotBlank()
            && surnameFlow.value.isNotBlank()
            && emailFlow.value.isNotBlank()
            && passwordFlow.value.isNotBlank()
            && requireStateModel().isEmailValid
            && requireStateModel().isEmailAlreadyTaken
        ) {
            stateModel = requireStateModel().copy(
                isFieldsValid = true,
                isEmailValid = true,
                isEmailAlreadyTaken = true
            )
        }
    }
}