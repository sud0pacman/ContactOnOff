package com.sudo_pacman.contactonoff.di

import com.sudo_pacman.contactonoff.navigation.AppNavigationHandler
import com.sudo_pacman.contactonoff.navigation.AppAppNavigationDispatcher
import com.sudo_pacman.contactonoff.navigation.AppNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    fun bindAppNavigator(impl: AppAppNavigationDispatcher): AppNavigator

    @Binds
    fun bindAppNavHandler(impl: AppAppNavigationDispatcher): AppNavigationHandler
}