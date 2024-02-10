package com.sudo_pacman.contactonoff.data

sealed interface ResultData<out T> {
    class Success<T>(val data: T) : ResultData<T>
    class Failure(val message: String) : ResultData<Nothing>

    fun onSuccess(block: (T) -> Unit): ResultData<T> {
        if (this is Success) block(this.data)
        return this
    }

    fun onFailure(block: (String) -> Unit): ResultData<T> {
        if (this is Failure) block(this.message)
        return this
    }

    companion object {
        fun <T> success(data: T) = Success(data)
        fun failure(message: String) = Failure(message)
    }
}