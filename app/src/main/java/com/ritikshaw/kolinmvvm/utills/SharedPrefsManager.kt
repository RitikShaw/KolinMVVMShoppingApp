package com.ritikshaw.kolinmvvm.utills

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.gson.Gson
import com.ritikshaw.kolinmvvm.activity.model.UserData

class SharedPrefsManager(context: Context) {

    private val prefs : SharedPreferences = context.getSharedPreferences("UserDetailsPref",Context.MODE_PRIVATE)

    fun saveUserDetails(userData : UserData){
        val userDataString = Gson().toJson(userData)
        prefs.edit().apply {
            putString("userData",userDataString)
            apply()
        }
    }

    fun getUserData(): LiveData<UserData>{
        return PreferenceLiveData(prefs,"userData",UserData()){ sharedPrefs, key, defaultValue ->
            Gson().fromJson(sharedPrefs.getString(key, null), UserData::class.java) ?: defaultValue
        }
    }

    fun clearData(){
        prefs.edit { clear() }

    }
    val userName : LiveData<String> = getUserData().map { it.name }

}