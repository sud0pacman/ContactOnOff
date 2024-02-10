package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.navigation.AppNavigator
import com.sudo_pacman.contactonoff.presenter.screens.sign_up.SignUpScreenDirections
import com.sudo_pacman.contactonoff.presenter.viewmodel.SignUpViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val navigate: AppNavigator
) : ViewModel(), SignUpViewModel {


    override fun signUp(firstName: String, lastName: String, phone: String, password: String) {
        "sign up  ".myLog()
        repository.registerUser2(firstName, lastName, phone, password).onEach {
            it.onSuccess {
                "sign up is succces $it".myLog()
                navigate.navigateTo(SignUpScreenDirections.actionSignUpScreenToVerifyScreen())
            }

            it.onFailure {
                "sign up model fail $it".myLog()
            }
        }.launchIn(viewModelScope)
    }

    override fun clickAllReady() {
        viewModelScope.launch {
            navigate.back()
        }
    }
}