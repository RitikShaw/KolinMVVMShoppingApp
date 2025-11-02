package com.ritikshaw.kolinmvvm.utills

import com.ritikshaw.kolinmvvm.activity.model.UserData

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userData : UserData) : AuthState()
    data class Error(val message: String) : AuthState()
}