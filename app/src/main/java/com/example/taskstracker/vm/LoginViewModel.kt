package com.example.taskstracker.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskstracker.data.login.LoginRepository
import com.example.taskstracker.data.room.Task
import com.example.taskstracker.data.room.TasksRepository
import com.example.taskstracker.utils.UiStatus
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tasksRepository: TasksRepository,
    private val coroutineDispatcher: CoroutineDispatcher
): ViewModel() {

    //encapsulation
    private val _loginState: MutableLiveData<UiStatus<FirebaseUser>> = MutableLiveData(UiStatus.LOADING)
    val loginState: LiveData<UiStatus<FirebaseUser>> get() = _loginState

    private val _logoutState: MutableLiveData<UiStatus<Unit>> = MutableLiveData(UiStatus.LOADING)
    val logoutState: LiveData<UiStatus<Unit>> get() = _logoutState

    private val _tasksState: MutableLiveData<UiStatus<List<Task>>> = MutableLiveData(UiStatus.LOADING)
    val tasksState: LiveData<UiStatus<List<Task>>> get() = _tasksState

    private val _insertTask: MutableLiveData<UiStatus<Unit>> = MutableLiveData(UiStatus.LOADING)
    val insertTask: LiveData<UiStatus<Unit>> get() = _insertTask

    private val _deleteTask: MutableLiveData<UiStatus<Unit>> = MutableLiveData(UiStatus.LOADING)
    val deleteTask: LiveData<UiStatus<Unit>> get() = _deleteTask

    fun authenticate(context: Context){
        viewModelScope.launch(coroutineDispatcher) {
            loginRepository.handleLogin(context).collect{
                _loginState.postValue(it)
            }
        }
    }

    fun logOut(){
        viewModelScope.launch(coroutineDispatcher) {
            loginRepository.handleLogout().collect{
                _logoutState.postValue(it)
            }
        }
    }

    fun getTasks(){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.getAllTasks().collect{
                _tasksState.postValue(it)
            }
        }
    }

    fun insertTask(task: Task){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.insertTask(task).collect{
                _insertTask.postValue(it)
            }
        }
    }

    fun deleteTask(task: Task){
        viewModelScope.launch(coroutineDispatcher) {
            tasksRepository.deleteTask(task).collect{
                _deleteTask.postValue(it)
            }
        }
    }

}

/*DOING AUTHENTICATE IN VIEWMODEL
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val auth: FirebaseAuth,
    private val coroutineDispatcher: CoroutineDispatcher //Always try to inject Dispatcher.Good for testing
) : ViewModel() {

    //Encapsulate the mutable and opens the immutable
    private val _userLoginStatus : MutableLiveData<UiStatus<FirebaseUser?>> = MutableLiveData(UiStatus.LOADING)
    val userLoginStatus : LiveData<UiStatus<FirebaseUser?>> get() = _userLoginStatus

    fun authenticate(credential: Credential){
        //Inject dispatcher here
        viewModelScope.launch(coroutineDispatcher) {
            val token = loginRepository.handleLogin(credential)
            val credentials = GoogleAuthProvider.getCredential(token, null)
            auth.signInWithCredential(credentials)
                .addOnCompleteListener{ task ->
                   if (task.isSuccessful) {
                       val user = auth.currentUser
                       _userLoginStatus.postValue(UiStatus.SUCCESS(user))
                   } else {
                       _userLoginStatus.postValue(UiStatus.ERROR(task.exception?: Exception("Login Failed ")))
                   }
                }
        }

    }


}
 */