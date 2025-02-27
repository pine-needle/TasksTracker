package com.example.taskstracker.di




import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.room.Room
import com.example.taskstracker.OAUTH_ID
import com.example.taskstracker.data.login.LoginRepository
import com.example.taskstracker.data.login.LoginRepositoryImpl
import com.example.taskstracker.data.room.TasksDao
import com.example.taskstracker.data.room.TasksDatabase
import com.example.taskstracker.data.room.TasksRepository
import com.example.taskstracker.data.room.TasksRepositoryImpl
import com.example.taskstracker.notifications.MessagingRepository
import com.example.taskstracker.notifications.MessagingRepositoryImpl
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesGson(): Gson = Gson()

    @Provides
    @Singleton
    fun providesGoogleId(): GetGoogleIdOption =
        GetGoogleIdOption.Builder()
            .setServerClientId(OAUTH_ID)
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .build()

    @Provides
    @Singleton
    fun providesCredentialRequest(
        googleIdOption: GetGoogleIdOption
    ): GetCredentialRequest = GetCredentialRequest
        .Builder()
        .addCredentialOption(googleIdOption)
        .build()

    @Provides
    @Singleton
    fun providesClearCredentialsState(): ClearCredentialStateRequest =
        ClearCredentialStateRequest()

    @Provides
    @Singleton
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideMessaging() : FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
        return CredentialManager.create(context)
    }

    @Provides
    fun providesLogin(
        auth: FirebaseAuth,
        credentialRequest: GetCredentialRequest,
        credentialManager: CredentialManager,
        clearCredentialStateRequest: ClearCredentialStateRequest
    ) : LoginRepository = LoginRepositoryImpl(auth, credentialRequest,credentialManager, clearCredentialStateRequest)

    @Provides
    fun providesCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun providesTaskDatabase(
        @ApplicationContext context: Context
    ): TasksDatabase =
        Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks-db"
        ).build()

    @Provides
    @Singleton
    fun providesDao(
        tasksDatabase: TasksDatabase
    ): TasksDao = tasksDatabase.getTasksDao()

    @Provides
    @Singleton
    fun providesTasksRepository(
        dao: TasksDao
    ): TasksRepository = TasksRepositoryImpl(dao)

    @Provides
    @Singleton
    fun providesMessagingRepository(
        firebaseMessaging: FirebaseMessaging
    ): MessagingRepository = MessagingRepositoryImpl(firebaseMessaging)
}
