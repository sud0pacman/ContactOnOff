package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse

interface ContactsViewModel  {
    val progressLiveData: LiveData<Boolean>
    val contactLiveData: LiveData<List<ContactUIData>>
    val errorMessageLiveData: LiveData<String>
    val notConnectionLiveData: LiveData<Unit>
    val emptyLiveData: LiveData<Unit>
    val openAddContactScreen: LiveData<Unit>

    fun loadContacts()
    fun openAddContactScreen()
    fun deleteContact(contactUIData: ContactUIData)
    fun clickEdit(contactUI: ContactUIData)
    val openEditScreen: MutableLiveData<ContactUIData>
}