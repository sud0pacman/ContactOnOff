package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudo_pacman.contactonoff.data.source.local.MyShar
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.VerifyViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(private val repository: ContactRepository) : ViewModel(), VerifyViewModel {
    override val isSuccessLivaData = MutableLiveData<Boolean>()

    override fun clickVerify(code: String) {
        "model click verify".myLog()

        repository.verifyUser2(code).onEach { responseResult ->
            responseResult.onSuccess {
                isSuccessLivaData.value = true
            }
            responseResult.onFailure {
                "verify error $it".myLog()
            }
        }.launchIn(viewModelScope)
    }
}