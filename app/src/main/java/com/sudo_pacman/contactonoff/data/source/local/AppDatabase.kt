package com.sudo_pacman.contactonoff.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sudo_pacman.contactonoff.data.source.local.dao.ContactDao
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao
}