package com.sudo_pacman.contactonoff.presenter.screens.login

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenLoginBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.LoginViewModelImpl
import com.sudo_pacman.contactonoff.utils.launch
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges

@AndroidEntryPoint
class LoginScreen : Fragment(R.layout.screen_login) {
    private val binding by viewBinding(ScreenLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModelImpl>()

    private lateinit var button: TextView
    private lateinit var scaleXAnimator: ObjectAnimator
    private lateinit var scaleYAnimator: ObjectAnimator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etPhone.text.toString(), binding.etPassword.text.toString())
        }

        initView()
    }

    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            viewModel.clickSignUp()
        }

        combine(
            binding.etPhone.textChanges()
                .filterNotNull()
                .map { it.length == 13 && it.startsWith("+998") },

            binding.etPassword.textChanges()
                .filterNotNull()
                .map { it.length > 4 },

            transform = { phone, password ->
                phone && password
            }
        ).launch(lifecycle, lifecycleScope) {
            binding.btnLogin.isEnabled = it
        }


        button = binding.btnLogin
    }
}