package com.sudo_pacman.contactonoff.data.source.remote.request

data class ContactCreateRequest(
    val firstName: String,
    val lastName: String,
    val phone: String
)