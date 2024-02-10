package com.sudo_pacman.contactonoff.navigation

import com.sudo_pacman.contactonoff.navigation.AppNavigation
import kotlinx.coroutines.flow.Flow

interface AppNavigationHandler {
    val buffer : Flow<AppNavigation>
}