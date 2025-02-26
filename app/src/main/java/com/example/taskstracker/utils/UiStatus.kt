package com.example.taskstracker.utils

sealed class UiStatus<out T> {

    data object LOADING : UiStatus<Nothing>()

    data class SUCCESS<T>(val message: T): UiStatus<T>()

    data class ERROR(val error:Exception) : UiStatus<Nothing>()
}