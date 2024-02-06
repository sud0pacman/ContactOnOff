package com.sudo_pacman.contactonoff.domain

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.sudo_pacman.contactonoff.data.mapper.ContactMapper.toUIData
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.data.model.toStatusEnum
import com.sudo_pacman.contactonoff.data.source.local.AppDataBase
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity
import com.sudo_pacman.contactonoff.data.source.remote.ApiClient
import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.EditContactRequest
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.DeleteContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.ErrorResponse
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import com.sudo_pacman.contactonoff.utils.myLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactRepositoryImpl private constructor() : ContactRepository {

    companion object {
        private lateinit var repository: ContactRepository

        fun init() {
            if (!(::repository.isInitialized)) repository = ContactRepositoryImpl()
        }

        fun getInstance() = repository
    }

    private val api = ApiClient.api
    val gson = Gson()
    private val contactDao = AppDataBase.getInstance().getContactDao()

    override fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit) {

        api.getAllContact().enqueue(object : Callback<List<ContactResponse>> {
            override fun onResponse(
                call: Call<List<ContactResponse>>,
                response: Response<List<ContactResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    "repo get success ${response.body()!!.size}".myLog()
                    successBlock.invoke(mergeData(response.body()!!, contactDao.getAllContactFromLocal()))
                } else if (response.errorBody() != null) {
//                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
//
//                    if (data != null) errorBlock.invoke(data.message)
//                    else errorBlock.invoke("Unknown error !")
                } else {
                    errorBlock.invoke("Unknown error!")
                }
            }

            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
                t.message?.let { errorBlock.invoke(it) }
            }

        })
    }

    @SuppressLint("BinaryOperationInTimber")
    override fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
        if (NetworkStatusValidator.hasNetwork) {

            val request = ContactCreateRequest(firstName, lastName, phone)
            api.addContact(request).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.errorBody() != null) {
                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        errorBlock.invoke(data.message)
                    } else {
                        errorBlock.invoke("Unknown error !!")
                    }
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    t.message?.let { errorBlock.invoke(it) }
                }
            })
        } else {
            contactDao.insertContact(ContactEntity(0, 0, firstName, lastName, phone, StatusEnum.ADD.statusCode))
            successBlock.invoke()
        }
    }

    override fun syncWithServer(finishBlock: () -> Unit, errorBlock: (String) -> Unit) {
        val localList = contactDao.getAllContactFromLocal()

        localList.forEach { entitity ->
            when (entitity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    api.addContact(ContactCreateRequest(entitity.firstName, entitity.lastName, entitity.phone))
                        .enqueue(object : Callback<ContactResponse> {
                            override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                                contactDao.deleteContact(entitity)
                                if (response.isSuccessful && response.body() != null) {
                                    finishBlock.invoke()
                                } else if (response.errorBody() != null) {
                                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                                    errorBlock.invoke(data.message)
                                } else errorBlock.invoke("Sync Unknown error!!")
                            }

                            override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                                t.message?.let { message -> errorBlock.invoke(message) }
                            }

                        })
                }

                StatusEnum.DELETE -> {
                    "sync delete id ${entitity.id}".myLog()
                    api.deleteContact(entitity.remoteID).enqueue(object : Callback<DeleteContactResponse> {
                        override fun onResponse(call: Call<DeleteContactResponse>, response: Response<DeleteContactResponse>) {
                            contactDao.deleteContact(entitity)

                            if (response.isSuccessful && response.body() != null) {
                                finishBlock.invoke()
                            } else if (response.errorBody() != null) {
                                val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                                errorBlock.invoke(data.message)
                            } else errorBlock.invoke("Sync delete Unknown error!!")
                        }

                        override fun onFailure(call: Call<DeleteContactResponse>, t: Throwable) {
                            t.message?.let { message -> errorBlock.invoke(message) }
                        }

                    })
                }
                StatusEnum.EDIT -> {
                    api.editContact(EditContactRequest(entitity.remoteID, entitity.firstName, entitity.lastName, entitity.phone)).enqueue(object : Callback<ContactResponse> {
                        override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                            contactDao.deleteContact(entitity)

                            if (response.isSuccessful && response.body() != null) {
                                finishBlock.invoke()
                            } else if (response.errorBody() != null) {
                                val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                                errorBlock.invoke(data.message)
                            } else errorBlock.invoke("Sync delete Unknown error!!")
                        }

                        override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                            t.message?.let { message -> errorBlock.invoke(message) }
                            "sync edit fail".myLog()
                        }

                    })
                }
                else -> {}
            }
        }
    }

    override fun deleteContact(contactUIData: ContactUIData, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
        if (NetworkStatusValidator.hasNetwork) {
            api.deleteContact(contactUIData.id).enqueue(object : Callback<DeleteContactResponse> {
                override fun onResponse(call: Call<DeleteContactResponse>, response: Response<DeleteContactResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.body() != null) {
                        errorBlock.invoke(response.errorBody().toString())
                    } else {
                        errorBlock.invoke("Delete Unknown error !!!")
                    }
                    "delete code: ${response.code()}".myLog()
                }

                override fun onFailure(call: Call<DeleteContactResponse>, t: Throwable) {
                    "repo delete failure".myLog()
                    t.message?.let { errorBlock.invoke(it) }
                }

            })
        } else {
            if (contactUIData.status == StatusEnum.ADD) {
                "repo: add deletega keldi".myLog()
                val localData = contactDao.getAllContactFromLocal()

                for (i in localData.indices) {
                    if (localData[i].phone == contactUIData.phone) {
                        contactDao.deleteContact(ContactEntity(localData[i].id, localData[i].remoteID, contactUIData.firstName, contactUIData.lastName, contactUIData.phone, StatusEnum.DELETE.statusCode))
                        break
                    }
                }

                successBlock.invoke()

                return
            }
            else {
                "repo delete offline".myLog()
                contactDao.insertContact(
                    ContactEntity(0, contactUIData.id, contactUIData.firstName, contactUIData.lastName, contactUIData.phone, StatusEnum.DELETE.statusCode)
                )

                successBlock.invoke()
            }
        }
    }

    override fun editContact(id: Int, firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
        if (NetworkStatusValidator.hasNetwork) {
            val editContactRequest = EditContactRequest(id, firstName, lastName, phone)

            api.editContact(editContactRequest).enqueue(object : Callback<ContactResponse> {
                override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
                    if (response.isSuccessful && response.body() != null) {
                        successBlock.invoke()
                    } else if (response.body() != null) {
                        errorBlock.invoke(response.errorBody().toString())
                    } else {
                        errorBlock.invoke("Edit Unknown error !!!")
                    }
                    "edit code: ${response.code()}".myLog()
                }

                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
                    "repo edit failure".myLog()
                    t.message?.let { errorBlock.invoke(it) }
                }

            })
        }
        else {
            contactDao.insertContact(
                ContactEntity(0, id, firstName, lastName, phone, StatusEnum.EDIT.statusCode)
            )

            successBlock.invoke()
        }
    }

    private fun mergeData(remoteList: List<ContactResponse>, localList: List<ContactEntity>): List<ContactUIData> {
        val result = ArrayList<ContactUIData>()
        result.addAll(remoteList.map { it.toUIData() })

        var index = remoteList.lastOrNull()?.id ?: 0    // fake

        localList.forEach { entity ->
            "merge foreach ${entity.statusCode.toStatusEnum()}".myLog()

            when (entity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    result.add(entity.toUIData(++index))
                }

                StatusEnum.EDIT -> {
                    val findData = result.find { it.id == entity.remoteID }

                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        "merge edited status ${newData.status.statusCode.toStatusEnum()}".myLog()
                        result[findIndex] = newData
                    }
                }

                StatusEnum.DELETE -> {
                    val findData = result.find { it.id == entity.remoteID }

                    "merge deleting contact ${findData.toString()}".myLog()

                    if (findData != null) {
                        val findIndex = result.indexOf(findData)
                        val newData = entity.toUIData(findData.id)
                        "merge deleted status ${newData.status.statusCode.toStatusEnum()}".myLog()
                        result[findIndex] = newData
                    }
                }

                StatusEnum.SYNC -> {}
            }
        }

        return result
    }
}