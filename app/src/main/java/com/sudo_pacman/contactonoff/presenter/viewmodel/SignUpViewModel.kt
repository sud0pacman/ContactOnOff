package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface SignUpViewModel {
    val clickSignUp: LiveData<Unit>
    val openLoginLiveData: MutableLiveData<Unit>
    val openVerifyLiveData: MutableLiveData<Boolean>

    fun signUp(firstName: String, lastName: String, phone: String, password: String)
    fun clickAllReady()
}