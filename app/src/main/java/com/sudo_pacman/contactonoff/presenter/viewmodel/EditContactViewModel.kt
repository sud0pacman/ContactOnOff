package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData
import com.sudo_pacman.contactonoff.data.model.StatusEnum

interface EditContactViewModel  {
    val closeScreenLiveData: LiveData<Unit>
    val progressLiveData: LiveData<Boolean>
    val messageLiveData: LiveData<String>
    val errorMessageLiveData: LiveData<String>

    fun closeScreen()
    fun editContact(id: Int, firsName: String, lastName: String, phone: String, status: StatusEnum)
}