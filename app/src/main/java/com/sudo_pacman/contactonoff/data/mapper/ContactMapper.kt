package com.sudo_pacman.contactonoff.data.mapper

import com.sudo_pacman.contactonoff.data.model.ContactUIData
import com.sudo_pacman.contactonoff.data.model.StatusEnum
import com.sudo_pacman.contactonoff.data.model.toStatusEnum
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity
import com.sudo_pacman.contactonoff.data.source.remote.response.ContactResponse

object ContactMapper {

    fun ContactResponse.toUIData() : ContactUIData =
        ContactUIData(
            id = this.id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = StatusEnum.SYNC
        )

    fun ContactEntity.toUIData(id : Int) : ContactUIData =
        ContactUIData(
            id = id,
            firstName = this.firstName,
            lastName = this.lastName,
            phone = this.phone,
            status = this.statusCode.toStatusEnum()
        )
}