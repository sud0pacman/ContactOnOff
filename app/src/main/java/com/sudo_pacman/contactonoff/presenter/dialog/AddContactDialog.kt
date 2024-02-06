package com.sudo_pacman.contactonoff.presenter.dialog

import AddViewModelFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.databinding.DialogAddContactBinding
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactAddViewModel
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.ContactAddViewModelImpl
import com.sudo_pacman.contactonoff.utils.myApply
import com.sudo_pacman.contactonoff.utils.myLog
import timber.log.Timber

class AddContactDialog : Fragment(R.layout.dialog_add_contact) {
    private val binding by viewBinding(DialogAddContactBinding::bind)
    private val viewModel by viewModels<ContactAddViewModelImpl> { AddViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.closeScreenLiveData.observe(this, closeScreenObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = binding.myApply {
        close.setOnClickListener { findNavController().popBackStack() }

        save.setOnClickListener {
            val fName = binding.firstName.text.toString()
            val lName = binding.lastName.text.toString()
            val phone = binding.number.text.toString()
            Timber.tag("BBB").d("fname ->" + fName)
            viewModel.addContact(fName,lName,phone)
        }

    }

    private val closeScreenObserver = Observer<Unit> { findNavController().navigateUp() }
    private val errorMessageObserver = Observer<String> {it.myLog()}
    private val messageObserver = Observer<String> {it.myLog()}
    private val progressLiveData = Observer<Boolean> {

    }

}