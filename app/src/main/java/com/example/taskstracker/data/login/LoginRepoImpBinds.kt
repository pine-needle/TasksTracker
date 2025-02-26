package com.example.taskstracker.data.login

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class LoginRepoImpBinds {
     //Another way of creating a repository module for injection
    //We use it by constructor injection without passing no parameters
    // eg: class LoginRepository@Inject constructor(): Repository {...}
//    @Binds
//    abstract  fun providesLogin(
//        loginRepository: LoginRepository
//    ) : LoginRepository
}