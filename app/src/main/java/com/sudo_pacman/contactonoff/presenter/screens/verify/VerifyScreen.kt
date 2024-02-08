package com.sudo_pacman.contactonoff.presenter.screens.verify

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenVerifyBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.VerifyViewModel
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.VerifyViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyScreen : Fragment(R.layout.screen_verify) {
    private val binding by viewBinding(ScreenVerifyBinding::bind)
    private val viewModel: VerifyViewModel  by viewModels<VerifyViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnVerify.setOnClickListener {
            viewModel.clickVerify(binding.etPassword.text.toString())
        }

        viewModel.isSuccessLivaData.observe(this, openVerifyObserver)
    }

    private val openVerifyObserver = Observer<Boolean> {
        if (it) findNavController().navigate(R.id.action_verifyScreen_to_contactsScreen)
    }
}