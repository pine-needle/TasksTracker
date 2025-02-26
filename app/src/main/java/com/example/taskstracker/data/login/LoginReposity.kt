package com.example.taskstracker.data.login

import android.content.Context
import com.example.taskstracker.utils.UiStatus
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
    suspend fun handleLogin(context: Context) : Flow<UiStatus<FirebaseUser>>

    suspend fun handleLogout(): Flow<UiStatus<Unit>>
}