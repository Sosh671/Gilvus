package com.soshdev.gilvus

import android.app.Application
import com.facebook.stetho.Stetho
import com.soshdev.gilvus.di.appModule
import com.soshdev.gilvus.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

@Suppress("unused")
class GilvusApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Timber
        if (BuildConfig.DEBUG)
            Timber.plant(DebugTree())

        // Koin
        startKoin {
            androidLogger()
            androidContext(this@GilvusApplication)
            modules(
                listOf(
                    appModule,
                    networkModule
                )
            )
        }

        // Stetho
        Stetho.initializeWithDefaults(this)
    }

    private class DebugTree : Timber.DebugTree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val tagWithPrefix = "Timber $tag"
            super.log(priority, tagWithPrefix, message, t)
        }
    }
}