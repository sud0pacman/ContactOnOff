package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.navigation.AppNavigator
import com.sudo_pacman.contactonoff.presenter.screens.contacts.ContactsScreenDirections
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactsViewModel
import com.sudo_pacman.contactonoff.utils.MyEventBus
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navigator: AppNavigator
) : ViewModel(), ContactsViewModel {
    override val progressFlow = MutableStateFlow(true)
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
        progressFlow.value = true

        repository.getAllContact2().onEach { resultData ->
            resultData.onSuccess {
                progressFlow.value = false
                contactLiveData.value = it
            }
            resultData.onFailure {
                "model get failure $it".myLog()
                progressFlow.value = false
            }

        }.launchIn(viewModelScope)
    }


    override fun openAddContactScreen() {
        openAddContactScreen.value = Unit
    }


    override fun deleteContact(contactUIData: ContactUIData) {
        progressFlow.value = true

        repository.deleteContact2(contactUIData).onEach {
            it.onSuccess {
                "model delete success".myLog()

                progressFlow.value = false
                loadContacts()
            }

            it.onFailure {
                "model delete error: $it".myLog()
                progressFlow.value = false
            }
        }.launchIn(viewModelScope)
    }

    override fun clickEdit(contactUI: ContactUIData) {


        if (contactUI.status == StatusEnum.ADD) {
            "model: bu kontakt add statusda buni faqat o'chirish mumkin".myLog()
            return
        }

        viewModelScope.launch {
            navigator.navigateTo(ContactsScreenDirections.actionContactsScreenToEditContactScreen(contactUI))
        }

        openEditScreen.value = contactUI
    }
}