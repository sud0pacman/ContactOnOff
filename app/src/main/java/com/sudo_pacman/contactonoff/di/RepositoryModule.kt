package com.sudo_pacman.contactonoff.di

import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.domain.ContactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getContactRepository(impl: ContactRepositoryImpl) : ContactRepository
}