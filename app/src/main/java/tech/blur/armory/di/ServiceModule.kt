package tech.blur.armory.di

import org.koin.dsl.module
import tech.blur.armory.data.services.booking.BookingService
import tech.blur.armory.data.services.login.LoginService
import tech.blur.armory.data.services.room.RoomService

val serviceModule = module {
    single { LoginService(get()) }
    single { RoomService(get()) }
    single { BookingService(get()) }
}