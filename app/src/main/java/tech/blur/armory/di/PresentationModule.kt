package tech.blur.armory.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.blur.armory.presentation.MainViewModel
import tech.blur.armory.presentation.book.BookViewModel
import tech.blur.armory.presentation.login.LoginViewModel
import tech.blur.armory.presentation.myEvents.MyEventsViewModel
import tech.blur.armory.presentation.registration.RegistrationViewModel
import tech.blur.armory.presentation.rooms.RoomsViewModel
import tech.blur.armory.presentation.settings.SettingsViewModel

val presentationModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegistrationViewModel(get()) }
    viewModel { RoomsViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { MyEventsViewModel(get(), get()) }
    viewModel { (id: Int) ->
        BookViewModel(
            androidContext().getSharedPreferences(
                "token",
                Context.MODE_PRIVATE
            ),
            get(),
            get(),
            id
        )
    }
}