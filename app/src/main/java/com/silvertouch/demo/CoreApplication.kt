package com.silvertouch.demo

import android.annotation.SuppressLint
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@SuppressLint("Registered")
class CoreApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CoreApplication)
            modules(listOf(dbModule, repositoryModule, uiModule))
        }
    }
}