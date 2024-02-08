package com.sudo_pacman.contactonoff.app

import android.app.Application
import com.sudo_pacman.contactonoff.data.source.local.MyShar
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        MyShar.init(this)

//        ContactRepositoryImpl.init()
    }
}