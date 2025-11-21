package com.ritikshaw.kolinmvvm.activity.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CameraSharedViewModel : ViewModel() {
    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap = _bitmap.asStateFlow()

    fun sendBitmap(bmp : Bitmap?){
        _bitmap.value = bmp
    }
}