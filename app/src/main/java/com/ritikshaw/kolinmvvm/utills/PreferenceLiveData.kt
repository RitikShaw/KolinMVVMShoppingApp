package com.ritikshaw.kolinmvvm.utills

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

class PreferenceLiveData<T>(
    private val sharedPrefs: SharedPreferences,
    private val key: String,
    private val defaultValue: T,
    private val getter: (SharedPreferences, String, T) -> T
) : LiveData<T>() {

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, changedKey ->
        if (changedKey == key) {
            value = getter(sharedPrefs, key, defaultValue)
        }
    }

    override fun onActive() {
        super.onActive()
        value = getter(sharedPrefs, key, defaultValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        super.onInactive()
    }
}