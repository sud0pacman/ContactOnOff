package com.sudo_pacman.contactonoff.data.source.remote.request

data class EditContactRequest(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String
)
