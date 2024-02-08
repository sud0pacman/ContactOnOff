package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData

interface SplashViewModel {
    val hasLogin : LiveData<Boolean>
}