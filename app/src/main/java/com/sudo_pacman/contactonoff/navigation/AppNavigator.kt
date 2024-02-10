package com.sudo_pacman.contactonoff.navigation

import androidx.navigation.NavDirections

interface AppNavigator {
    suspend fun navigateTo(direction: NavDirections)
    suspend fun back()
}