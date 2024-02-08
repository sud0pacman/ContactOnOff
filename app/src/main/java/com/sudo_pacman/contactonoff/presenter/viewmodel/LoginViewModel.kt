package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData

interface LoginViewModel {
    val clickLogIn: LiveData<Boolean>
    val clickSignUp: LiveData<Boolean>

    fun login(phone: String, password: String)
}