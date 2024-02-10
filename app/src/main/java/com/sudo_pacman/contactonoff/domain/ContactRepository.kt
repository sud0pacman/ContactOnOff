package com.sudo_pacman.contactonoff.domain

import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.DeleteContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.RegisterResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
//    fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit)
    fun getAllContact2() : Flow<Result<List<ContactUIData>>>

//    fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun addContact2(firstName: String, lastName: String, phone: String) : Flow<Result<ContactResponse>>

//    fun editContact(id: Int, firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun editContact2(id: Int, firstName: String, lastName: String, phone: String) : Flow<Result<ContactResponse>>

    fun syncWithServer() : Flow<Result<Unit>>

//    fun deleteContact(contactUIData: ContactUIData, successBlock: () -> Unit, errorBlock: (String) -> Unit)
    fun deleteContact2(contactUIData: ContactUIData) : Flow<Result<DeleteContactResponse>>

//    fun registerUser(firstName: String, lastName: String, phone: String, password: String, success: () -> Unit, failure: (String) -> Unit)
    fun registerUser2(firstName: String, lastName: String, phone: String, password: String) : Flow<Result<RegisterResponse>>

//    fun login(phone: String, password: String, success: () -> Unit, failure: (String) -> Unit)
    fun login2(phone: String, password: String, ) : Flow<Result<TokenResponse>>

//    fun verifyUser(phone: String, code: String, success: () -> Unit, failure: (String) -> Unit)
    fun verifyUser2(code: String) : Flow<Result<TokenResponse>>
    fun networkNo()
}