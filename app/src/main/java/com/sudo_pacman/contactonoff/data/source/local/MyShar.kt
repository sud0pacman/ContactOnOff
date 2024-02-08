package com.sudo_pacman.contactonoff.data.source.local

import android.content.Context
import android.content.SharedPreferences

object MyShar {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("Contact", Context.MODE_PRIVATE)
    }

    fun setToken(token: String) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    fun getToken(): String = sharedPreferences.getString("token", "")!!

    fun setCode(code: String) = sharedPreferences.edit().putString("code", code).apply()

    fun getCode(): String = sharedPreferences.getString("code", "").toString()

    fun setPhone(code: String) = sharedPreferences.edit().putString("phone", code).apply()
    fun getPhone(): String = sharedPreferences.getString("phone", "").toString()
}