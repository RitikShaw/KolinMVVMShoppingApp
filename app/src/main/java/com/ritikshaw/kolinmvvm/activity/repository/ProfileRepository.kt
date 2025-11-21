package com.ritikshaw.kolinmvvm.activity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.ritikshaw.kolinmvvm.activity.model.UserData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ProfileRepository(
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    fun getUserFromFirebase(uId : String): LiveData<Result<UserData>>{

        val liveData = MutableLiveData<Result<UserData>>()
        val userRef = firebaseDatabase.getReference("userDetails")
        val valueListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.child(uId).getValue(UserData::class.java)
                if (userData!=null){
                    liveData.postValue(Result.success(userData))
                }
                else{
                    liveData.postValue(Result.failure(Exception("User not found")))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                liveData.postValue(Result.failure(Exception(error.message)))
            }
        }
        userRef.addValueEventListener(valueListener)

        return liveData
    }

    suspend fun updateUserData(userData: UserData): Result<String>{
        return suspendCancellableCoroutine { continuation ->
            val userRef = firebaseDatabase.getReference("userDetails")
            userRef.child(userData.userId).setValue(userData)
                .addOnSuccessListener {
                    if (continuation.isActive){
                        continuation.resume(Result.success(userData.userId))
                    }
                }
                .addOnFailureListener {
                    if (continuation.isActive){
                        continuation.resume(Result.failure(Exception(it.message)))
                    }
                }
        }
    }

    suspend fun uploadImage(biteArray : ByteArray,userData: UserData): Result<String>{
        val imageRef = FirebaseStorage.getInstance().reference
        val fileRef = imageRef.child("${userData.userId}.jpg")
        return suspendCancellableCoroutine { continuation ->
            fileRef.putBytes(biteArray)
                .addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { downloadUrl->
                        if (continuation.isActive){
                            continuation.resume(Result.success(downloadUrl.toString()))
                        }
                    }
                        .addOnFailureListener {
                            if (continuation.isActive){
                                continuation.resume(Result.failure(Exception(it.message)))
                            }
                        }
                }
                .addOnFailureListener {
                    if (continuation.isActive){
                        continuation.resume(Result.failure(Exception(it.message)))
                    }
                }
        }


    }
}