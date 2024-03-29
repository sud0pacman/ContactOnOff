package com.sudo_pacman.contactonoff.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContactUIData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val status: StatusEnum
): Parcelable