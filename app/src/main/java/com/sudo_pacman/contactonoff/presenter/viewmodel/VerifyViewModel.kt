package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData

interface VerifyViewModel {
    val isSuccessLivaData: LiveData<Boolean>

    fun clickVerify(code: String)
}