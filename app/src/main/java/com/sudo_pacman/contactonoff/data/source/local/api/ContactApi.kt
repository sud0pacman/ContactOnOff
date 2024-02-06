package com.sudo_pacman.contactonoff.data.source.local.api

import com.sudo_pacman.contactonoff.data.source.remote.request.ContactCreateRequest
import com.sudo_pacman.contactonoff.data.source.remote.request.EditContactRequest
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse
import com.sudo_pacman.contactonoff.data.source.remote.response.DeleteContactResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ContactApi {
    @GET("/api/v1/contact")
    fun getAllContact(): Call<List<ContactResponse>>

    @POST("/api/v1/contact")
    fun addContact(@Body data: ContactCreateRequest): Call<ContactResponse>

    @DELETE("/api/v1/contact")
    fun deleteContact(@Query("id") id: Int): Call<DeleteContactResponse>

    @PUT("/api/v1/contact")
    fun editContact(@Body data: EditContactRequest): Call<ContactResponse>
}