package tech.blur.armory.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tech.blur.armory.data.providers.ResourceProvider

val providerModule = module {
    single { ResourceProvider(androidApplication()) }
}