package com.example.taskstracker.data.notifications

import com.example.taskstracker.utils.UiStatus
import kotlinx.coroutines.flow.Flow

interface MessagingRepository {
    fun getToken(): Flow<UiStatus<String>>
}