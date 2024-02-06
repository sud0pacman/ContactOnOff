package com.sudo_pacman.contactonoff.utils

import timber.log.Timber

fun <T> T.myApply(block: T.() -> Unit) {
    block(this)
}


fun String.myLog(tag: String = "TTT") = Timber.tag(tag).d(this)
