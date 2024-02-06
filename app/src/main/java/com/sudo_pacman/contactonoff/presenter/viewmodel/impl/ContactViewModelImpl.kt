package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactsViewModel
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import com.sudo_pacman.contactonoff.utils.myLog
import timber.log.Timber

class ContactViewModelImpl(private val repository: ContactRepository) : ViewModel(), ContactsViewModel {
    override val progressLiveData = MutableLiveData<Boolean>()
    override val contactLiveData = MutableLiveData<List<ContactUIData>>()
    override val errorMessageLiveData = MutableLiveData<String>()
    override val notConnectionLiveData = MutableLiveData<Unit>()
    override val emptyLiveData = MutableLiveData<Unit>()
    override val openAddContactScreen = MutableLiveData<Unit>()
    override val openEditScreen = MutableLiveData<ContactUIData>()


    init {
        MyEventBus.reload = {
            loadContacts()
        }
    }

    override fun loadContacts() {
        progressLiveData.value = true
        repository.getAllContact(
            successBlock = {
                Timber.tag("TTT").d("viewModel get succes %s", it.size)
                contactLiveData.value = it
                progressLiveData.value = false
            },
            errorBlock = {
                Timber.tag("TTT").d("viewModel get error %s", it)
                progressLiveData.value = false
            }
        )
    }

    override fun openAddContactScreen() {
        openAddContactScreen.value = Unit
    }

    override fun deleteContact(contactUIData: ContactUIData) {
        progressLiveData.value = true

        repository.deleteContact(
            contactUIData = contactUIData,
            successBlock = {
                "model delete success".myLog()

                progressLiveData.value = false
                loadContacts()
            },
            errorBlock = {
                "model delete error: $it".myLog()
                progressLiveData.value = false
            }
        )
    }

    override fun clickEdit(contactUI: ContactUIData) {
        if(contactUI.status == StatusEnum.ADD) {
            "model: bu kontakt add statusda buni faqat o'chirish mumkin".myLog()
            return
        }

        openEditScreen.value = contactUI
    }
}