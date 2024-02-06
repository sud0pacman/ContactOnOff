package com.sudo_pacman.contactonoff.presenter.screens.contacts

import EditViewModelFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.ScreenContactEditBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.EditContactViewModelImpl
import com.sudo_pacman.contactonoff.utils.myApply
import com.sudo_pacman.contactonoff.utils.myLog

class EditContactScreen : Fragment(R.layout.screen_contact_edit) {
    private val binding by viewBinding(ScreenContactEditBinding::bind)
    private val viewModel by viewModels<EditContactViewModelImpl> { EditViewModelFactory() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        val id = requireArguments().getInt("contactID")
        "screen edit id $id".myLog()
        binding.inputName.setText(requireArguments().getString("firstname"))
        binding.inputLastName.setText(requireArguments().getString("lastname"))
        binding.inputPhone.setText(requireArguments().getString("phone"))

        buttonBack.setOnClickListener { findNavController().popBackStack() }

        buttonEdit.setOnClickListener {
            val fName = binding.inputName.text.toString()
            val lName = binding.inputLastName.text.toString()
            val phone = binding.inputPhone.text.toString()
            viewModel.editContact(id, fName, lName, phone)
        }
    }


    private val closeScreenObserver = Observer<Unit> {
        findNavController().popBackStack()
    }
}