package com.ritikshaw.kolinmvvm.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritikshaw.kolinmvvm.activity.model.UserData
import com.ritikshaw.kolinmvvm.activity.repository.ProfileRepository
import com.ritikshaw.kolinmvvm.utills.AuthState
import com.ritikshaw.kolinmvvm.utills.GeneralSealedClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    val profileRepository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _profileState = MutableStateFlow<AuthState>(AuthState.Idle)
    val profileState get() = _profileState.asStateFlow()

    private val _userUpdated = MutableStateFlow<GeneralSealedClass<String>>(GeneralSealedClass.Idle())
    val userUpdated get() = _userUpdated.asStateFlow()
    fun getUserDataFromFirebase(uId : String){
        viewModelScope.launch {
            _profileState.value = AuthState.Loading
            val response = profileRepository.getUserFromFirebase(uId)
            response.observeForever { result ->
                _profileState.value = result.fold(
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

    fun updateProfile(userData : UserData){
        viewModelScope.launch {
            _userUpdated.value = GeneralSealedClass.Idle()
            val response = profileRepository.updateUserData(userData)
            _userUpdated.value = response.fold(
                onSuccess = { result->
                    GeneralSealedClass.Success(result)
                },
                onFailure = {
                    GeneralSealedClass.Error(it.message?:"Unknown Error")
                }
            )
        }
    }

    fun uploadImage(byteArray: ByteArray,userData: UserData){
        viewModelScope.launch {
            val response = profileRepository.uploadImage(byteArray,userData)
            response.onSuccess {
                updateProfile(userData.copy(profilePicture = it))
            }
            response.onFailure {
                _profileState.value = AuthState.Error(it.message?:"Unknown Error")
            }
        }
    }
}