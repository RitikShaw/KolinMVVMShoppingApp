package com.ritikshaw.kolinmvvm.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.ritikshaw.kolinmvvm.activity.model.UserData
import com.ritikshaw.kolinmvvm.activity.repository.AuthenticationRepository
import com.ritikshaw.kolinmvvm.utills.AuthState
import com.ritikshaw.kolinmvvm.utills.GeneralSealedClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authenticationRepository: AuthenticationRepository = AuthenticationRepository()
) : ViewModel() {


    private val _authStateSignUp = MutableStateFlow<GeneralSealedClass<Pair<FirebaseUser?, UserData?>>>(GeneralSealedClass.Idle())
    val authStateSignUp get() = _authStateSignUp.asStateFlow()

    private val _authStateLogin = MutableStateFlow<AuthState>(AuthState.Idle)
    val authStateLogin get() = _authStateLogin.asStateFlow()

    fun registerWithEmail(email:String , password : String){
        viewModelScope.launch {
            _authStateSignUp.value = GeneralSealedClass.Loading
            val result = authenticationRepository.registerWithEmail(email,password)
            _authStateSignUp.value = result.fold(
                onSuccess = { userId-> GeneralSealedClass.Success(
                    Pair(null,UserData(
                        userId = userId,
                        name = "",
                        email = email,
                        phoneNumber = "",
                        profilePicture = "",
                        password = password
                    ))
                )},
                onFailure = { GeneralSealedClass.Error(it.message ?: "Unknown Error")}
            )
        }
    }

    fun loginWithEmail(email:String , password : String){
        viewModelScope.launch {
            _authStateLogin.value = AuthState.Loading
            val result = authenticationRepository.loginWithEmail(email,password)
            result.onSuccess { userId->
                getUserDataFromFirebase(userId)
            }
            result.onFailure {
                _authStateLogin.value = AuthState.Error(it.message?:"Unknown Error")
            }
        }
    }

    fun signInWithGoogle(idToken : String) {
        viewModelScope.launch {
            _authStateSignUp.value = GeneralSealedClass.Loading
            val result = authenticationRepository.signInWithGoogle(idToken)
            result.onSuccess { response ->
               try {
                   val user = response.first
                   val isNewUser = response.second
                   if (isNewUser){
                       _authStateSignUp.value = GeneralSealedClass.Success(Pair(user,null))
                   }
                   else{
                       _authStateSignUp.value = GeneralSealedClass.Error("User already exists")
                   }
               }
               catch (e: Exception){
                   e.printStackTrace()
                   _authStateSignUp.value = GeneralSealedClass.Error(e.message?:"Unknown Error")
               }
           }
            result.onFailure {
                _authStateSignUp.value = GeneralSealedClass.Error(it.message?:"Unknown Error")
            }
        }
    }

    fun createPassword(user: FirebaseUser, password: String){
        viewModelScope.launch {
            _authStateSignUp.value = GeneralSealedClass.Loading
            val result = authenticationRepository.createPassword(user,password)
            result.fold(
                onSuccess = { data->
                    createUserIntoFirebase(data)
                },
                onFailure = {
                    _authStateSignUp.value = GeneralSealedClass.Error(it.message?:"Unknown Error")
                }
            )
        }
    }
    fun loginInWithGoogle(idToken : String) {
        viewModelScope.launch {
            _authStateLogin.value = AuthState.Loading
            val result = authenticationRepository.signInWithGoogle(idToken)
            result.onSuccess { response ->
                try {
                    val user = response.first
                    val userData = UserData(
                        userId = user.uid,
                        name = user.displayName?:"",
                        email = user.email?:"",
                        password = "password"
                    )
                    getUserDataFromFirebase(userData.userId)
                }
                catch (e: Exception){
                    e.printStackTrace()
                    _authStateLogin.value = AuthState.Error(e.message?:"Unknown Error")
                }
            }
            result.onFailure {
                _authStateLogin.value = AuthState.Error(it.message?:"Unknown Error")
            }
        }
    }

    fun createUserIntoFirebase(userData : UserData) {
        viewModelScope.launch {
            _authStateSignUp.value = GeneralSealedClass.Loading
            val result = authenticationRepository.createUserIntoFirebase(userData)
            result.fold(
                onSuccess =  {
                    val observer = authenticationRepository.getUserFromFirebase(userData.userId)
                    observer.observeForever { innerResult->
                        innerResult.fold(
                            onSuccess = { user->
                                _authStateSignUp.value = GeneralSealedClass.Success(Pair(null,user))
                            },
                            onFailure = {
                                _authStateSignUp.value = GeneralSealedClass.Error(it.message?:"Unknown Error")
                            }
                        )
                    }
                },
                onFailure = { _authStateSignUp.value = GeneralSealedClass.Error(it.message?:"Unknown Error")}
            )
        }
    }

    fun getUserDataFromFirebase(uId : String){
        viewModelScope.launch {
            _authStateLogin.value = AuthState.Loading
            val observer = authenticationRepository.getUserFromFirebase(uId)
            observer.observeForever { result->
                _authStateLogin.value = result.fold(
                    onSuccess = { userData ->
                        AuthState.Success(userData)
                    },
                    onFailure = {
                        AuthState.Error(it.message?:"Unknown Error")
                    }
                )
            }
        }
    }

}