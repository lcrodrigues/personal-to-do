package com.example.personaltodo.core.util

sealed class AppState<out T> {
    object Init: AppState<Nothing>()
    object Loading: AppState<Nothing>()
    object Empty: AppState<Nothing>()
    data class Success<T>(val data: T): AppState<T>()
    data class GenericError<T>(val data: T?): AppState<T>()
    data class InternetError<T>(val data: T?): AppState<T>()
}