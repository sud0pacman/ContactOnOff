package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.EditContactViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditContactViewModelImpl @Inject constructor(private val repository: ContactRepository) : EditContactViewModel, ViewModel() {
    override val closeScreenLiveData = MutableLiveData<Unit>()
    override val progressLiveData = MutableLiveData<Boolean>()
    override val messageLiveData = MutableLiveData<String>()
    override val errorMessageLiveData = MutableLiveData<String>()

    override fun closeScreen() {
        closeScreenLiveData.value = Unit
    }

    override fun editContact(id: Int, firsName: String, lastName: String, phone: String) {
        repository.editContact(
            id = id,
            firstName = firsName,
            lastName = lastName,
            phone = phone,
            successBlock = {
                "model edit success".myLog()
                closeScreenLiveData.value = Unit
            },
            errorBlock = {
                "model edit error $it".myLog()
            }
        )
    }
}