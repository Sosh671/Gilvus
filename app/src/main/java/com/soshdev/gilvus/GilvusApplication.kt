package com.soshdev.gilvus

import android.app.Application
import com.facebook.stetho.Stetho
import com.soshdev.gilvus.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class GilvusApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber
        if (BuildConfig.DEBUG)
            Timber.plant(Timber.DebugTree())

        // Koin
        startKoin{
            androidLogger()
            androidContext(this@GilvusApplication)
            modules(appModule)
        }

        // Stetho
        Stetho.initializeWithDefaults(this)
    }
}