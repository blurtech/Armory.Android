package tech.blur.armory.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import tech.blur.armory.data.db.Database
import tech.blur.armory.data.storages.UserStorage

val storageModule = module {
    single {
        Room.databaseBuilder(androidApplication(), Database::class.java, Database.NAME)
            .build()
    }

    single { get<Database>().userDao() }

    single { UserStorage(get()) }
}
