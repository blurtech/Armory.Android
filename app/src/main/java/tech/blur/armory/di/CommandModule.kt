package tech.blur.armory.di

import android.content.Context
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tech.blur.armory.domain.commands.*

val commandModule = module {
    single { GoogleCalendarCommand(get(), androidApplication()) }
    single { LoginCommand(get(), get()) }
    single { RoomCommand(get()) }
    single {
        LogOutCommand(
            get(),
            androidApplication().getSharedPreferences("token", Context.MODE_PRIVATE)
        )
    }
    single { BookingCommand(get()) }
}