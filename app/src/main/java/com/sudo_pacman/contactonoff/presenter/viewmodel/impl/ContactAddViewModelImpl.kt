package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactAddViewModel
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactAddViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val networkStatusValidator: NetworkStatusValidator
) : ViewModel(), ContactAddViewModel {
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
                if (networkStatusValidator.hasNetwork) messageLiveData.value = "Success!"
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