package com.ritikshaw.kolinmvvm.activity.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ritikshaw.kolinmvvm.activity.model.UserData
import com.ritikshaw.kolinmvvm.utills.SharedPrefsManager

class SharedPreferenceViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPrefsManager= SharedPrefsManager(application)

    val getUserData = sharedPrefsManager.getUserData()
    val userNAme = sharedPrefsManager.userName

    suspend fun saveUserData(userData: UserData){
        sharedPrefsManager.saveUserDetails(userData)
    }

}