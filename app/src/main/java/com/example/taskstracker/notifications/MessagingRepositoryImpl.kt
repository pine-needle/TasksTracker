package com.example.taskstracker.notifications

import com.example.taskstracker.utils.UiStatus
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagingRepositoryImpl@Inject constructor (
    private val firebaseMessaging: FirebaseMessaging
) : MessagingRepository{
    override fun getToken(): Flow<UiStatus<String>> = flow{
        emit(UiStatus.LOADING)
        try {
            //Fetching token
            val token = firebaseMessaging.token.await()
            emit(UiStatus.SUCCESS(token))
        }catch (e:Exception){
            emit(UiStatus.ERROR(e))
        }
    }
}