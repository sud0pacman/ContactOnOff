package com.sudo_pacman.contactonoff.data.source.remote.request

data class VerifyRequest(
    val phone: String,
    val code: String
)