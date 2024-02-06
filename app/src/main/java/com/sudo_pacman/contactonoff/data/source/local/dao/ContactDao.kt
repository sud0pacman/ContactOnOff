package com.sudo_pacman.contactonoff.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity

@Dao
interface ContactDao {
    @Query("SELECT * FROM contactentity")
    fun getAllContactFromLocal() : List<ContactEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertContact(data: ContactEntity)

    @Delete
    fun deleteContact(data: ContactEntity)

    @Update
    fun editContact(data: ContactEntity)
}