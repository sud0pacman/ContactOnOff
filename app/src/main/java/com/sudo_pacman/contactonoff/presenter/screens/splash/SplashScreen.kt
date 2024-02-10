package com.sudo_pacman.contactonoff.presenter.screens.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenSplashBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.SplashViewModel
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.SplashViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment(R.layout.screen_splash) {
    private val viewModel: SplashViewModel by viewModels<SplashViewModelImpl>()
    private val binding: ScreenSplashBinding by viewBinding(ScreenSplashBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }


    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.hasLogin.observe(this@SplashScreen, loginStateObServer)
    }

    private val loginStateObServer = Observer<Boolean> {
        navController.navigate(SplashScreenDirections.actionSplashScreenToLoginScreen())
    }
}