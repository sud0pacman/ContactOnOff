package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.SignUpViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val repository: ContactRepository
) : ViewModel(), SignUpViewModel {
    override val clickSignUp = MutableLiveData<Unit>()
    override val openVerifyLiveData = MutableLiveData<Boolean>()
    override val openLoginLiveData = MutableLiveData<Unit>()

    override fun signUp(firstName: String, lastName: String, phone: String, password: String) {
        repository.registerUser(firstName, lastName, phone, password,
            success = {
                openVerifyLiveData.value = true
            },
            failure = {
                "sign up model fail $it".myLog()
            }
        )
    }

    override fun clickAllReady() {
        openLoginLiveData.value = Unit
    }
}