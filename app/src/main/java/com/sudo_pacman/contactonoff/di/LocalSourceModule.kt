package com.sudo_pacman.contactonoff.di

import android.content.Context
import androidx.room.Room
import com.sudo_pacman.contactonoff.data.source.local.AppDatabase
import com.sudo_pacman.contactonoff.data.source.local.dao.ContactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalSourceModule {

    @[Provides Singleton]
    fun provideAppDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "MyContact.db")
            .allowMainThreadQueries()
            .build()


    @[Provides Singleton]
    fun provideContactDao(database: AppDatabase) : ContactDao = database.getContactDao()
}