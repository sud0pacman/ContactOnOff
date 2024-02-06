package com.sudo_pacman.contactonoff.data.source.remote.response

import com.sudo_pacman.contactonoff.data.source.local.MyShar

data class TokenResponse(
    val token: String,
    val phone: String
){
    // shardga saqla
    init {
        MyShar.setToken(token)
    }
}
