package tech.blur.armory.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tech.blur.armory.domain.commands.GoogleCalendarCommand
import tech.blur.armory.domain.commands.LogOutCommand
import tech.blur.armory.domain.commands.LoginCommand
import tech.blur.armory.domain.commands.RoomCommand

val commandModule = module {
    single { GoogleCalendarCommand(get(), androidApplication()) }
    single { LoginCommand(get(), get()) }
    single { RoomCommand(get()) }
    single { LogOutCommand(get()) }
}