package com.sudo_pacman.contactonoff.utils

object MyEventBus {
    var reload: (() -> Unit)? = null
}