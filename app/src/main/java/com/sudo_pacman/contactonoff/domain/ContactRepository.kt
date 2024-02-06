package com.sudo_pacman.contactonoff.domain

import com.sudo_pacman.contactonoff.data.model.ContactUIData

interface ContactRepository {
    fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit)
    fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun syncWithServer(finishBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun deleteContact(contactUIData: ContactUIData, successBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun editContact(id: Int, firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit)
}