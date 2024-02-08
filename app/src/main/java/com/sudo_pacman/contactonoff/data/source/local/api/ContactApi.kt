package com.sudo_pacman.contactonoff.data.source.local.api

import com.sudo_pacman.contactonoff.data.source.remote.request.LoginRequest
import com.example.onlinecontact.data.global.request.RegisterRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.VerifyRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.EditContactRequest
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.DeleteContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.TokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ContactApi {

    // register
    @POST("/api/v1/register")
    fun registerUser(@Body data: RegisterRequest): Call<Unit>

    // verify
    @POST("/api/v1/register/verify")
    fun verifyUser(@Body verifyRequest: VerifyRequest): Call<TokenResponse>

    // login
    @POST("/api/v1/login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<TokenResponse>

    @GET("/api/v1/contact")
    fun getAllContact(@Header("token") token: String): Call<List<ContactResponse>>

    @POST("/api/v1/contact")
    fun addContact(@Header("token") token: String, @Body data: ContactCreateRequest): Call<ContactResponse>

    @DELETE("/api/v1/contact")
    fun deleteContact(@Header("token") token: String, @Query("id") id: Int): Call<DeleteContactResponse>

    @PUT("/api/v1/contact")
    fun editContact(@Header("token") token: String, @Body data: EditContactRequest): Call<ContactResponse>
}