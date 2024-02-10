package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface SignUpViewModel {
    fun signUp(firstName: String, lastName: String, phone: String, password: String)
    fun clickAllReady()
}