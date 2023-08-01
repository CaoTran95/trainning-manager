package com.example.trainingmanager.base

abstract class BaseUseCase<T> {
    abstract suspend fun execute(vararg params: Any): T
}