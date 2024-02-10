package com.sudo_pacman.contactonoff.presenter.viewmodel

interface LoginViewModel {
    fun clickSignUp()
    fun login(phone: String, password: String)
}