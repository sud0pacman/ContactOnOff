package com.sudo_pacman.contactonoff.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val remoteID: Int,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val statusCode: Int
)