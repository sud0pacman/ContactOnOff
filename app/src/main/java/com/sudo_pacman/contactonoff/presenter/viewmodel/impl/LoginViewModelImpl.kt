package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.navigation.AppNavigator
import com.sudo_pacman.contactonoff.presenter.screens.login.LoginScreenDirections
import com.sudo_pacman.contactonoff.presenter.viewmodel.LoginViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModelImpl @Inject constructor(
    private val repository: ContactRepository,
    private val appNavigator: AppNavigator
) : ViewModel(), LoginViewModel {

    override fun login(phone: String, password: String) {

        repository.login2(phone, password).onEach {
            it.onSuccess {
                "model login success".myLog()
                appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToContactsScreen())
            }

            it.onFailure {
                "model login fail".myLog()
            }
        }.launchIn(viewModelScope)

    }

    override  fun clickSignUp() {
        viewModelScope.launch {
            appNavigator.navigateTo(LoginScreenDirections.actionLoginScreenToSignUpScreen())
        }
    }
}