package com.ritikshaw.kolinmvvm.utills

sealed class GeneralSealedClass<out T> {
    class Idle : GeneralSealedClass<Nothing>()
    object Loading : GeneralSealedClass<Nothing>()
    data class Success<T>(val data:T):GeneralSealedClass<T>()
    data class Error<T>(val message:String):GeneralSealedClass<T>()
}