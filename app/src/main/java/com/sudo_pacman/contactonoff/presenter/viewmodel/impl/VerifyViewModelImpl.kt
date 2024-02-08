package com.sudo_pacman.contactonoff.presenter.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudo_pacman.contactonoff.data.source.local.MyShar
import com.sudo_pacman.contactonoff.domain.ContactRepository
import com.sudo_pacman.contactonoff.presenter.viewmodel.VerifyViewModel
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyViewModelImpl @Inject constructor(private val repository: ContactRepository) : ViewModel(), VerifyViewModel {
    override val isSuccessLivaData = MutableLiveData<Boolean>()

    override fun clickVerify(code: String) {
        repository.verifyUser(
            phone = MyShar.getPhone(),
            code = code,
            success = {
                isSuccessLivaData.value = true
            },
            failure = {
                "verify error $it".myLog()
            }
        )
    }
}