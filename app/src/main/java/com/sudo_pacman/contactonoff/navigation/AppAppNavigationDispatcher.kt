package com.sudo_pacman.contactonoff.navigation

import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAppNavigationDispatcher @Inject constructor() : AppNavigator, AppNavigationHandler {
    override val buffer = MutableSharedFlow<AppNavigation>()

    private suspend fun navigate(navigation: AppNavigation) {
        buffer.emit(navigation)
    }

    override suspend fun navigateTo(direction: NavDirections) = navigate {
        navigate(direction)
    }

    override suspend fun back() = navigate {
        popBackStack()
    }
}