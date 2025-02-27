package com.example.taskstracker.data.login


import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.taskstracker.utils.UiStatus
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class LoginRepositoryImpl@Inject constructor(
    private  val auth: FirebaseAuth,
    private val credentialRequest: GetCredentialRequest,
    private  val credentialManager: CredentialManager,
    private val clearCredentialStateRequest: ClearCredentialStateRequest

) : LoginRepository {

    override suspend fun handleLogin(context: Context): Flow<UiStatus<FirebaseUser>> = flow{
        emit(UiStatus.LOADING)
        try {
            auth.currentUser?.let {
                emit(UiStatus.SUCCESS(it))
            } ?: run {
                val credentialResult = credentialManager.getCredential(
                    context,
                    credentialRequest
                )
                val credential = credentialResult.credential
                if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    val idToken = GoogleIdTokenCredential.createFrom(credential.data).idToken
                    val credentials = GoogleAuthProvider.getCredential(idToken,null)
                    val result = auth.signInWithCredential(credentials).await()
                    result.user?.let {
                        emit(UiStatus.SUCCESS(it))
                    } ?: throw Exception("User is null")
                } else throw Exception("Credential is not of type Google")
            }
        } catch (e: Exception){
            emit(UiStatus.ERROR(e))
        }
    }

    //Handling logout
    override suspend fun handleLogout(): Flow<UiStatus<Unit>> = flow {
        emit(UiStatus.LOADING)
        try {
            auth.signOut()
            credentialManager.let {
                it.clearCredentialState(clearCredentialStateRequest)
                emit(UiStatus.SUCCESS(Unit))
            } ?: throw Exception("Credential manager doesn't exist")
        } catch (e: Exception){
            emit(UiStatus.ERROR(e))
        }
    }


}