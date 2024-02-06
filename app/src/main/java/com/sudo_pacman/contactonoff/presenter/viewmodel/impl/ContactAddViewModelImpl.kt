package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactAddViewModel
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import com.sudo_pacman.contactonoff.utils.myLog
import timber.log.Timber

class ContactAddViewModelImpl(private val repository: ContactRepository) : ContactAddViewModel, ViewModel() {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()


    override fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    override fun addContact(firsName: String, lastName: String, phone: String) {
        progressLiveData.value = true
        repository.addContact(
            firstName = firsName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                progressLiveData.value = true
                if (NetworkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
                else messageLiveData.value = "Save in local"

                closeScreenLiveData.value = Unit
                MyEventBus.reload?.invoke()
            },
            errorBlock = {
                progressLiveData.value = false
                errorMessageLiveData.value = it
                it.myLog()
            }
        )
    }
}