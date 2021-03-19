package tech.blur.armory.di

import org.koin.dsl.module
import tech.blur.armory.data.services.login.LoginService

val serviceModule = module {
    single { LoginService(get()) }
}