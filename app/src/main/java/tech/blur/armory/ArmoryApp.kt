package tech.blur.armory

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tech.blur.armory.di.*

class ArmoryApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)

            modules(listOf(networkModule,
                commandModule,
                providerModule,
                serviceModule,
                storageModule,
                presentationModule))
        }
    }
}