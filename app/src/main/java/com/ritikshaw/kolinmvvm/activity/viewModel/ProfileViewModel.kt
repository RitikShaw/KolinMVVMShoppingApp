package com.ritikshaw.kolinmvvm.activity.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ritikshaw.kolinmvvm.activity.repository.ProfileRepository
import com.ritikshaw.kolinmvvm.utills.AuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    val profileRepository: ProfileRepository = ProfileRepository()
) : ViewModel() {

    private val _profileState = MutableStateFlow<AuthState>(AuthState.Idle)
    val profileState get() = _profileState.asStateFlow()

    fun getUserDataFromFirebase(uId : String){
        viewModelScope.launch {
            _profileState.value = AuthState.Loading
            val result = profileRepository.getUserFromFirebase(uId)
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