package com.sudo_pacman.contactonoff.presenter.screens.contacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sudo_pacman.contactonoff.R
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.databinding.ScreenMainBinding
import com.sudo_pacman.contactonoff.presenter.adapter.ContactAdapter
import com.sudo_pacman.contactonoff.presenter.dialog.EventDialog
import com.sudo_pacman.contactonoff.presenter.viewmodel.ContactsViewModel
import com.sudo_pacman.contactonoff.presenter.viewmodel.impl.ContactViewModelImpl
import com.sudo_pacman.contactonoff.utils.launch
import com.sudo_pacman.contactonoff.utils.myLog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsScreen : Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private val viewModel: ContactsViewModel by viewModels<ContactViewModelImpl>()
    private val adapter by lazy { ContactAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.errorMessageLiveData.observe(this, errorMessageObserver)
        viewModel.notConnectionLiveData.observe(this, notConnectionObserver)
        viewModel.openAddContactScreen.observe(this, openAddScreenObserver)
        viewModel.openEditScreen.observe(this, openEditScreenObserver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())

        viewModel.loadContacts()

        viewModel.progressFlow.launch(lifecycle, lifecycleScope) {
            binding.vRefresh.isRefreshing = it
        }

        viewModel.emptyLiveData.observe(viewLifecycleOwner, emptyStateObserver)
        viewModel.contactLiveData.observe(viewLifecycleOwner, contactsObserver)

        binding.buttonAdd.setOnClickListener {
            viewModel.openAddContactScreen()
        }

        binding.vRefresh.setOnRefreshListener {
            binding.vRefresh.isRefreshing = true
            viewModel.loadContacts()
        }

        adapter.clickMore = { contactUI ->
            val eventDialog = EventDialog()

            eventDialog.setClickDeleteButtonListener {
                viewModel.deleteContact(contactUI)
            }

            eventDialog.setClickEditButtonListener {
                viewModel.clickEdit(contactUI)
            }

            eventDialog.show(childFragmentManager, null)
        }
    }

    private val errorMessageObserver = Observer<String> {

    }

    private val notConnectionObserver = Observer<Unit> {

    }


    private val emptyStateObserver = Observer<Unit> {

    }

    private val contactsObserver = Observer<List<ContactUIData>> {
        "view get success".myLog("SSS")
        adapter.submitList(it)
    }

    private val openAddScreenObserver = Observer<Unit> {
        findNavController().navigate(R.id.action_contactsScreen_to_addContactDialog)
    }

    private val openEditScreenObserver = Observer<ContactUIData> {
//        findNavController().navigate(ContactsScreenDirections.actionContactsScreenToEditContactScreen(it.id, it.firstName, it.lastName, it.phone))
//        findNavController().findDestination(ContactsScreenDirections.actionContactsScreenToEditContactScreen())
//        findNavController().navigate(R.id.action_contactsScreen_to_editContactScreen)
    }
}