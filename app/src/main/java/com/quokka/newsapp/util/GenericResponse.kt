package com.quokka.newsapp.util

sealed class GenericResponse<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Success<T>(data: T) : GenericResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : GenericResponse<T>(data, message)
    class PreExecute<T> : GenericResponse<T>()
}