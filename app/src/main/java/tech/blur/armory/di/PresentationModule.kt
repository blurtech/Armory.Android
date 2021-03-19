package tech.blur.armory.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.blur.armory.presentation.MainViewModel
import tech.blur.armory.presentation.login.LoginViewModel
import tech.blur.armory.presentation.registration.RegistrationViewModel

val presentationModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
}