package com.sudo_pacman.contactonoff.utils

import com.google.gson.Gson
import com.sudo_pacman.contactonoff.data.ResultData
import com.sudo_pacman.contactonoff.data.source.remote.response.ErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response

val gson = Gson()

fun <T, R> Response<T>.toResultData(block: (T) -> R): ResultData<R> {
    return when (code()) {
        in 200 until 300 -> ResultData.success(block(body()!!))
        in 400 until 500 -> {
            val data = gson.fromJson(errorBody()?.string().toString(), ErrorResponse::class.java)
            ResultData.failure(data.message)
        }

        else -> ResultData.failure("Server bilan bog'lanishda xatolik!")
    }
}

fun <T> flowResponse(block: suspend FlowCollector<ResultData<T>>.() -> Unit) = flow(block).flowOn(Dispatchers.IO).catch { emit(ResultData.failure(it.message.toString())) }