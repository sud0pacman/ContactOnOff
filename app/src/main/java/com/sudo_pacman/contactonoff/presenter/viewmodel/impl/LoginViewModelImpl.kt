package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.LoginViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(private val repository: ContactRepository) : ViewModel(), LoginViewModel {
    override val clickLogIn = MutableLiveData<Boolean>()

    override val clickSignUp = MutableLiveData<Boolean>()

    override fun login(phone: String, password: String) {
        repository.login(phone, password,
            success = {
                clickLogIn.value = true
            },
            failure = {

            }
            )
    }
}