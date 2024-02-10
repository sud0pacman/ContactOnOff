package com.sudo_pacman.contactonoff.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface ContactsViewModel  {
    val progressFlow: StateFlow<Boolean>
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