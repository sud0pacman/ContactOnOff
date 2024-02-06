package com.sudo_pacman.contactonoff.app

import android.app.Application
import com.sudo_pacman.contactonoff.data.source.local.AppDataBase
import com.sudo_pacman.contactonoff.data.source.remote.ApiClient
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import timber.log.Timber

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        ApiClient.init(this)
        AppDataBase.init(this)
        ContactRepositoryImpl.init()
    }
}