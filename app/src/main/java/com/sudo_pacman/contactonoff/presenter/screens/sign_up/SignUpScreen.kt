package com.sudo_pacman.contactonoff.presenter.screens.sign_up

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.data.source.local.MyShar
import com.sudo_pacman.contactonoff.databinding.ScreenSignUpBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.SignUpViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignUpScreen : Fragment(R.layout.screen_sign_up) {
    private val binding by viewBinding(ScreenSignUpBinding::bind)
    private val viewModel by viewModels<SignUpViewModelImpl>()

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSignUp.setOnClickListener {
            val firstName = binding.etFirstname.text.toString()
            val lastName = binding.etLastname.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()

            Timber.tag("TTT").d("click sign up in sign up")
            viewModel.signUp(firstName, lastName, phone, password)
            MyShar.setPhone(phone)
        }


        binding.btnAllReady.setOnClickListener {
            viewModel.clickAllReady()
        }
    }
}