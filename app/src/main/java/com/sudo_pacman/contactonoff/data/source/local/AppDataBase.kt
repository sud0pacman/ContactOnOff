package com.sudo_pacman.contactonoff.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sudo_pacman.contactonoff.data.source.local.dao.ContactDao
import com.sudo_pacman.contactonoff.data.source.local.entity.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun getContactDao(): ContactDao

    companion object {
        private lateinit var instance: AppDataBase

        fun init(context: Context) {
            if (!(::instance.isInitialized))
                instance = Room.databaseBuilder(context, AppDataBase::class.java, "contacts.db")
                    .allowMainThreadQueries()
                    .build()
        }

        fun getInstance() = instance
    }
}