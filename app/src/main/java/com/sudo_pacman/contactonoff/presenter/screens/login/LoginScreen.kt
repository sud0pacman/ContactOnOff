package com.sudo_pacman.contactonoff.presenter.screens.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenLoginBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.LoginViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModelImpl>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etPhone.text.toString(),
                binding.etPassword.text.toString()
            )
        }

        initView()
        initObserve()
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initObserve() {
        viewModel.clickLogIn.observe(this, loginObserver)

        viewModel.clickSignUp.observe(this, signUpObserver)
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    private val loginObserver = Observer<Boolean> {
        if (it) findNavController().navigate(R.id.action_loginScreen_to_contactsScreen)
    }

    private val signUpObserver = Observer<Boolean> {
        if (it) findNavController().popBackStack()
    }
}