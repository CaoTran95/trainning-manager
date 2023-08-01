package com.example.trainingmanager.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        Log.e("Error","coroutineException: $e")
    }

    protected fun launchCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch(coroutineExceptionHandler + dispatcher) {
            block()
        }
    }

    suspend fun <T> ResultApi<T>.subscribe(
        onSuccess: (suspend (T?) -> Unit)? = null,
        onError: (suspend (String?) -> Unit)? = null,
        onErrorType: (suspend (error: String?, String?) -> Unit)? = null,
        onComplete: (suspend () -> Unit)? = null
    ) {
        when (this) {
            is ResultApi.Success -> {
                onSuccess?.invoke(value)
            }
            is ResultApi.Error -> {
                when (code) {
                    ErrorCode.NETWORK.code -> {
                        // TODO handle network error
                    }

                }
                onError?.invoke(message)
                onErrorType?.invoke(error, message)
            }
        }
        onComplete?.invoke()
    }
}