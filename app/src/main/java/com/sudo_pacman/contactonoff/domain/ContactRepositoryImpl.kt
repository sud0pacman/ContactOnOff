package com.sudo_pacman.contactonoff.domain

import com.example.onlinecontact.data.global.request.RegisterRequest
import com.google.gson.Gson
import com.sudo_pacman.contactonoff.data.mapper.ContactMapper.toUIData
import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.data.model.toStatusEnum
import com.sudo_pacman.contactonoff.data.source.local.MyShar
import com.sudo_pacman.contactonoff.data.source.local.MyShar.getPhone
import com.sudo_pacman.contactonoff.data.source.local.MyShar.getToken
import com.sudo_pacman.contactonoff.data.source.local.api.ContactApi
import com.sudo_pacman.contactonoff.data.source.local.dao.ContactDao
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity
import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.EditContactRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.LoginRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.VerifyRequest
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.DeleteContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.ErrorResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.RegisterResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.TokenResponse
import com.sudo_pacman.contactonoff.utils.NetworkStatusValidator
import com.sudo_pacman.contactonoff.utils.myLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDao: ContactDao,
    private val api: ContactApi,
    private val gson: Gson,
    private val networkStatusValidator: NetworkStatusValidator
) : ContactRepository {


    override fun getAllContact2(): Flow<Result<List<ContactUIData>>> = flow {
        "repo token ${getToken()}".myLog()

        val response = api.getAllContact2(getToken())

        if (response.isSuccessful && response.body() != null) {
            "repo get success ${response.body()!!.size}".myLog()
            emit(Result.success(mergeData(response.body()!!, contactDao.getAllContactFromLocal())))
        }
        else {
            emit(Result.failure(Throwable(response.message())))
        }
    }.flowOn(Dispatchers.IO)
        .catch { emit(Result.failure(Throwable(it.message.toString()))) }


    override fun addContact2(firstName: String, lastName: String, phone: String): Flow<Result<ContactResponse>> = flow {

        if (networkStatusValidator.hasNetwork) {
            val contactContactRequest = ContactCreateRequest(firstName, lastName, phone)
            val contactResponse = api.addContact2(getToken(), contactContactRequest)
            if (contactResponse.isSuccessful && contactResponse.body() != null) {
                emit(Result.success(contactResponse.body()!!))
            } else {
                emit(Result.failure(Throwable(contactResponse.message())))
            }
        } else {
            contactDao.insertContact(ContactEntity(0, 0, firstName, lastName, phone, StatusEnum.ADD.statusCode))

            emit(Result.failure(Throwable("Not connection")))
        }
    }.flowOn(Dispatchers.IO)

    override fun syncWithServer(): Flow<Result<Unit>> = flow<Result<Unit>> {
        val localList = contactDao.getAllContactFromLocal()

        localList.forEach { entitity ->
            when (entitity.statusCode.toStatusEnum()) {
                StatusEnum.ADD -> {
                    contactDao.deleteContact(entitity)
                    val contactContactRequest = ContactCreateRequest(entitity.firstName, entitity.lastName, entitity.phone)
                    val contactResponse = api.addContact2(getToken(), contactContactRequest)
                    if (contactResponse.isSuccessful && contactResponse.body() != null) {
                        emit(Result.success(Unit))
                    } else {
                        emit(Result.failure(Throwable(contactResponse.message())))
                    }
                }

                StatusEnum.DELETE -> {
                    contactDao.deleteContact(entitity)
                    "sync delete id ${entitity.id}".myLog()
                    val deleteRequest = entitity.remoteID

                    val deleteResponse = api.deleteContact2(getToken(), deleteRequest)

                    if (deleteResponse.isSuccessful && deleteResponse.body() != null) {
                        emit(Result.success(Unit))
                    }
                    else {
                        emit(Result.failure(Throwable(deleteResponse.message())))
                    }
                }

                StatusEnum.EDIT -> {
                    contactDao.deleteContact(entitity)
                    val contactResponse = api.editContact2(getToken(), EditContactRequest(entitity.remoteID, entitity.firstName, entitity.lastName, entitity.phone))

                    if (contactResponse.isSuccessful && contactResponse.body() != null) {
                        emit(Result.success(Unit))
                    }
                    else {
                        emit(Result.failure(Throwable(contactResponse.message())))
                    }
                }

                else -> {}
            }
        }
    }.flowOn(Dispatchers.IO)


    override fun deleteContact2(contactUIData: ContactUIData): Flow<Result<DeleteContactResponse>> = flow {
        if (networkStatusValidator.hasNetwork) {
            val deleteRequest = contactUIData.id

            val deleteResponse = api.deleteContact2(getToken(), deleteRequest)

            if (deleteResponse.isSuccessful && deleteResponse.body() != null) {
                emit(Result.success(deleteResponse.body()!!))
            }
            else {
                emit(Result.failure(Throwable(deleteResponse.message())))
            }
        }
        else {
            if (contactUIData.status == StatusEnum.ADD) {
                "repo: add deletega keldi".myLog()
                val localData = contactDao.getAllContactFromLocal()

                for (i in localData.indices) {
                    if (localData[i].phone == contactUIData.phone) {
                        contactDao.deleteContact(
                            ContactEntity(
                                localData[i].id,
                                localData[i].remoteID,
                                contactUIData.firstName,
                                contactUIData.lastName,
                                contactUIData.phone,
                                StatusEnum.DELETE.statusCode
                            )
                        )
                        break
                    }
                }

                emit(Result.failure(Throwable("Not connecting and adding status")))
            } else {
                "repo delete offline".myLog()
                contactDao.insertContact(
                    ContactEntity(
                        0,
                        contactUIData.id,
                        contactUIData.firstName,
                        contactUIData.lastName,
                        contactUIData.phone,
                        StatusEnum.DELETE.statusCode
                    )
                )

                emit(Result.failure(Throwable("Not connection"))) // +998 91 157 0964
            }
        }
    }

    override fun editContact2(id: Int, firstName: String, lastName: String, phone: String, ): Flow<Result<ContactResponse>> = flow {
        if (networkStatusValidator.hasNetwork) {
            val contactResponse = api.editContact2(getToken(), EditContactRequest(id, firstName, lastName, phone))

            if (contactResponse.isSuccessful && contactResponse.body() != null) {
                emit(Result.success(contactResponse.body()!!))
            }
            else {
                emit(Result.failure(Throwable(contactResponse.message())))
            }
        }
        else {
            contactDao.insertContact(
                ContactEntity(0, id, firstName, lastName, phone, StatusEnum.EDIT.statusCode)
            )

            emit(Result.failure(Throwable("Not connection")))
        }
    }.flowOn(Dispatchers.IO)

    override fun registerUser2(firstName: String, lastName: String, phone: String, password: String): Flow<Result<RegisterResponse>> = flow {
        "repo register2".myLog()

        val registerRequest = RegisterRequest(firstName, lastName, phone, password)
        val registerResponse = api.registerUser2(registerRequest)

        if (registerResponse.isSuccessful && registerResponse.body() != null) {
            emit(Result.success(registerResponse.body()!!))
        }
        else {
            emit(Result.failure(Throwable(registerResponse.message())))
        }
    }.flowOn(Dispatchers.IO)

    override fun verifyUser2(code: String): Flow<Result<TokenResponse>> = flow {
        "repo verify network ok".myLog()
        val verifyRequest = VerifyRequest(getPhone(), code)

        val verifyResponse = api.verifyUser2(verifyRequest)

        "repo verify response ${verifyResponse.message()}, ${verifyResponse.code()}".myLog()

        if (verifyResponse.isSuccessful && verifyResponse.body() != null) {
            MyShar.setToken(verifyResponse.body()!!.token)
            emit(Result.success(verifyResponse.body()!!))
        }
        else {
            emit(Result.failure(Throwable(verifyResponse.message())))
        }
    }.flowOn(Dispatchers.IO)


    override fun login2(phone: String, password: String): Flow<Result<TokenResponse>> = flow {
        if (networkStatusValidator.hasNetwork) {
            "repo login net yes".myLog()
            val loginRequest = LoginRequest(phone, password)
            val loginResponse = api.loginUser2(loginRequest)

            if (loginResponse.isSuccessful && loginResponse.body() != null) {
                emit(Result.success(loginResponse.body()!!))
            }
            else {
                emit(Result.failure(Throwable(loginResponse.message())))
            }
        }
        else {
            emit(Result.failure(Throwable("No connection")))
        }
    }.flowOn(Dispatchers.IO)

    override fun networkNo()  {
        "repo internet o'chdi".myLog()
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

    private fun getFailureMessage(errorBody: ResponseBody?): String {
        try {
            if (errorBody != null) {
                val data = gson.fromJson(errorBody.string(), ErrorResponse::class.java)
                return data?.message ?: "Not Connection Internet"
            }
        } catch (_: Exception) {

        }
        return "Nomalum xatolik!"
    }


//    override fun getAllContact(successBlock: (List<ContactUIData>) -> Unit, errorBlock: (String) -> Unit) {
//
//        "repo token ${getToken()}".myLog()
//
//        api.getAllContact(getToken()).enqueue(object : Callback<List<ContactResponse>> {
//            override fun onResponse(
//                call: Call<List<ContactResponse>>,
//                response: Response<List<ContactResponse>>
//            ) {
//                if (response.isSuccessful && response.body() != null) {
//                    "repo get success ${response.body()!!.size}".myLog()
//                    successBlock.invoke(mergeData(response.body()!!, contactDao.getAllContactFromLocal()))
//                } else if (response.errorBody() != null) {
////                    val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
////
////                    if (data != null) errorBlock.invoke(data.message)
////                    else errorBlock.invoke("Unknown error !")
//                } else {
//                    errorBlock.invoke("Unknown error!")
//                }
//            }
//
//            override fun onFailure(call: Call<List<ContactResponse>>, t: Throwable) {
//                t.message?.let { errorBlock.invoke(it) }
//            }
//
//        })
//    }


//    override fun addContact(firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
//        if (networkStatusValidator.hasNetwork) {
//
//            val request = ContactCreateRequest(firstName, lastName, phone)
//            api.addContact(getToken(), request).enqueue(object : Callback<ContactResponse> {
//                override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        successBlock.invoke()
//                    } else if (response.errorBody() != null) {
//                        val data = gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
//                        errorBlock.invoke(data.message)
//                    } else {
//                        errorBlock.invoke("Unknown error !!")
//                    }
//                }
//
//                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
//                    t.message?.let { errorBlock.invoke(it) }
//                }
//            })
//        } else {
//            contactDao.insertContact(ContactEntity(0, 0, firstName, lastName, phone, StatusEnum.ADD.statusCode))
//            successBlock.invoke()
//        }
//    }

//    override fun deleteContact(contactUIData: ContactUIData, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
//        if (networkStatusValidator.hasNetwork) {
//            api.deleteContact(getToken(), contactUIData.id).enqueue(object : Callback<DeleteContactResponse> {
//                override fun onResponse(call: Call<DeleteContactResponse>, response: Response<DeleteContactResponse>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        successBlock.invoke()
//                    } else if (response.body() != null) {
//                        errorBlock.invoke(response.errorBody().toString())
//                    } else {
//                        errorBlock.invoke("Delete Unknown error !!!")
//                    }
//                    "delete code: ${response.code()}".myLog()
//                }
//
//                override fun onFailure(call: Call<DeleteContactResponse>, t: Throwable) {
//                    "repo delete failure".myLog()
//                    t.message?.let { errorBlock.invoke(it) }
//                }
//
//            })
//        } else {
//            if (contactUIData.status == StatusEnum.ADD) {
//                "repo: add deletega keldi".myLog()
//                val localData = contactDao.getAllContactFromLocal()
//
//                for (i in localData.indices) {
//                    if (localData[i].phone == contactUIData.phone) {
//                        contactDao.deleteContact(
//                            ContactEntity(
//                                localData[i].id,
//                                localData[i].remoteID,
//                                contactUIData.firstName,
//                                contactUIData.lastName,
//                                contactUIData.phone,
//                                StatusEnum.DELETE.statusCode
//                            )
//                        )
//                        break
//                    }
//                }
//
//                successBlock.invoke()
//
//                return
//            } else {
//                "repo delete offline".myLog()
//                contactDao.insertContact(
//                    ContactEntity(
//                        0,
//                        contactUIData.id,
//                        contactUIData.firstName,
//                        contactUIData.lastName,
//                        contactUIData.phone,
//                        StatusEnum.DELETE.statusCode
//                    )
//                )
//
//                successBlock.invoke()
//            }
//        }
//    }

//    override fun editContact(id: Int, firstName: String, lastName: String, phone: String, successBlock: () -> Unit, errorBlock: (String) -> Unit) {
//        if (networkStatusValidator.hasNetwork) {
//            val editContactRequest = EditContactRequest(id, firstName, lastName, phone)
//
//            api.editContact(MyShar.getToken(), editContactRequest).enqueue(object : Callback<ContactResponse> {
//                override fun onResponse(call: Call<ContactResponse>, response: Response<ContactResponse>) {
//                    if (response.isSuccessful && response.body() != null) {
//                        successBlock.invoke()
//                    } else if (response.body() != null) {
//                        errorBlock.invoke(response.errorBody().toString())
//                    } else {
//                        errorBlock.invoke("Edit Unknown error !!!")
//                    }
//                    "edit code: ${response.code()}".myLog()
//                }
//
//                override fun onFailure(call: Call<ContactResponse>, t: Throwable) {
//                    "repo edit failure".myLog()
//                    t.message?.let { errorBlock.invoke(it) }
//                }
//
//            })
//        } else {
//            contactDao.insertContact(
//                ContactEntity(0, id, firstName, lastName, phone, StatusEnum.EDIT.statusCode)
//            )
//
//            successBlock.invoke()
//        }
//    }

//    override fun registerUser(firstName: String, lastName: String, phone: String, password: String, success: () -> Unit, failure: (String) -> Unit) {
//        val registerRequest = RegisterRequest(firstName, lastName, phone, password)
//        api.registerUser(registerRequest).enqueue(object : Callback<Unit> {
//            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
//                if (response.isSuccessful) {
//                    success.invoke()
//                } else failure.invoke(getFailureMessage(response.errorBody()))
//            }
//
//            override fun onFailure(call: Call<Unit>, t: Throwable) {
//                failure.invoke(t.message ?: "")
//            }
//        })
//    }

//    override fun verifyUser(phone: String, code: String, success: () -> Unit, failure: (String) -> Unit) {
//        val verifyRequest = VerifyRequest(phone, code)
//        api.verifyUser(verifyRequest).enqueue(object : Callback<TokenResponse> {
//            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                if (response.isSuccessful) {
//                    MyShar.setToken(response.body()!!.token)
//
//                    if (response.isSuccessful) success.invoke()
//                    else failure.invoke(getFailureMessage(response.errorBody()))
//                }
//            }
//
//            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                failure.invoke(t.message ?: "")
//            }
//        })
//    }

//    override fun login(phone: String, password: String, success: () -> Unit, failure: (String) -> Unit) {
//        if (networkStatusValidator.hasNetwork) {
//            val loginRequest = LoginRequest(phone, password)
//            api.loginUser(loginRequest).enqueue(object : Callback<TokenResponse> {
//                override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
//                    if (response.isSuccessful) {
//                        "repo login success".myLog()
//                        MyShar.setToken(response.body()!!.token)
//
//                        if (response.isSuccessful) success.invoke()
//                        else failure.invoke(getFailureMessage(response.errorBody()))
//                    }
//                }
//
//                override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
//                    failure.invoke(t.message ?: "")
//                }
//
//            })
//        } else {
//            failure.invoke("Inter yo'q")
//        }
//    }
}