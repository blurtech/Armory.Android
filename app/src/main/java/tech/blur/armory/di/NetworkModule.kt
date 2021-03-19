package tech.blur.armory.di

import org.koin.dsl.module
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.login.AuthApi

val networkModule = module {
    single { AuthApi(get()) }

    single { HttpClientProvider() }
}