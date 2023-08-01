package com.example.trainingmanager.utils

import com.example.trainingmanager.base.ErrorCode
import com.example.trainingmanager.base.ResponseApi
import com.example.trainingmanager.base.ResultApi
import com.example.trainingmanager.data.model.BaseData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Returns a list containing all elements except last [n] elements.
 */
fun <E> List<E>.removeLast(n: Int): MutableList<E> {
    if (n > size) {
        throw IllegalArgumentException("Requested element $n is larger than list size $size")
    }
    if (n < 0) {
        throw IllegalArgumentException("Requested element $n is less than zero")
    }
    return dropLast(size - n).toMutableList()
}

fun <E> List<E>?.ifNullOrEmpty(default: List<E>) =
    if (isNullOrEmpty()) default else this

inline fun <reified T> Gson.fromJson(json: String?): T? {
    val type = object : TypeToken<T>() {}.type
    return fromJson(json, type)
}

inline fun <T, R> T.runCatching(block: T.() -> R): ResultApi<R> {
    return try {
        val response = block()
        ResultApi.Success(response)
    } catch (ex: Exception) {
        val errorResult = ex.toErrorResult()
        errorResult
    }
}

fun Exception.toErrorResult() =
    when (this) {
        is HttpException -> {
            val errorBody = response()?.errorBody()?.string()
            val response = Gson().fromJson<ResponseApi<Nothing>>(errorBody)
            ResultApi.Error(
                code = response?.code ?: code(),
                message = response?.message ?: message(),
                error = response?.error
            )
        }
        is IOException -> ResultApi.Error(
            code = ErrorCode.NETWORK.code,
            message = message
        )
        is SocketTimeoutException -> ResultApi.Error(
            code = ErrorCode.TIMEOUT.code,
            message = message
        )
        else -> ResultApi.Error(
            code = ErrorCode.UNKNOWN.code,
            message = message
        )
    }

fun <T> ResponseApi<T>.toErrorResult() =
    ResultApi.Error(
        code = code ?: ErrorCode.UNKNOWN.code,
        message = message,
        error = error
    )
