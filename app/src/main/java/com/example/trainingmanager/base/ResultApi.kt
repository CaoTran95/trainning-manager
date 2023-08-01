package com.example.trainingmanager.base

sealed class ResultApi<out T> {
    class Success<T>(val value: T?) : ResultApi<T>()
    class Error(val code: Int = -1, val message: String? = null, val error: String? = null) :
        ResultApi<Nothing>()
}

enum class ErrorCode(val code: Int) {
    GENERIC(511),
    NETWORK(512),
    TIMEOUT(513),
    UNKNOWN(-1)
}

data class ResponseApi<T>(
    val code: Int? = null,
    val data: T? = null,
    val message: String? = null,
    val error: String? = null
) {
    val isSuccessful: Boolean
        get() = code in 200..299
}