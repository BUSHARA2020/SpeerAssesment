package com.example.speeronlinetest.api

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Loading<out T>(val data: T? = null) : Resource<T>()
    data class Error(val message: String, val data: Nothing? = null) : Resource<Nothing>()

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Success(data)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Loading(data)
        }

        fun error(message: String): Resource<Nothing> {
            return Error(message)
        }
    }
}
