package tech.blur.armory.di

import org.koin.dsl.module
import tech.blur.armory.data.db.Database
import tech.blur.armory.data.db.Database_Impl
import tech.blur.armory.data.storages.UserStorage

val storageModule = module {
    single<Database> { Database_Impl() }
    single { get<Database>().userDao() }

    single { UserStorage(get()) }
}
