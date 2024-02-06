package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData

interface ContactAddViewModel {
    val closeScreenLiveData: LiveData<Unit>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>

    fun closeScreen()
    fun addContact(firsName: String, lastName: String, phone: String)
}