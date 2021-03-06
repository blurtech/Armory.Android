package tech.blur.armory.di

import org.koin.dsl.module
import tech.blur.armory.data.providers.HttpClientProvider
import tech.blur.armory.data.services.booking.BookingApi
import tech.blur.armory.data.services.login.AuthApi
import tech.blur.armory.data.services.room.RoomApi
import tech.blur.armory.data.services.user.UserApi

val networkModule = module {
    single { AuthApi(get()) }
    single { RoomApi(get(), get()) }
    single { BookingApi(get(), get()) }
    single { UserApi(get(), get()) }

    single { HttpClientProvider() }
}