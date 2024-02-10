package com.sudo_pacman.contactonoff.presenter.screens.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenContactEditBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.EditContactViewModelImpl
import com.sudo_pacman.contactonoff.utils.myApply
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditContactScreen : Fragment(R.layout.screen_contact_edit) {
    private val binding by viewBinding(ScreenContactEditBinding::bind)
    private val viewModel by viewModels<EditContactViewModelImpl>()
    private val navArgs by navArgs<EditContactScreenArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        val data =  navArgs.ContactUIData
        val id = data.id
        "screen edit id $id".myLog()
        binding.inputName.setText(data.firstName)
        binding.inputLastName.setText(data.lastName)
        binding.inputPhone.setText(data.phone)

        buttonBack.setOnClickListener { findNavController().popBackStack() }

        buttonEdit.setOnClickListener {
            val fName = binding.inputName.text.toString()
            val lName = binding.inputLastName.text.toString()
            val phone = binding.inputPhone.text.toString()
            viewModel.editContact(id, fName, lName, phone, data.status)
        }

    }


    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
}