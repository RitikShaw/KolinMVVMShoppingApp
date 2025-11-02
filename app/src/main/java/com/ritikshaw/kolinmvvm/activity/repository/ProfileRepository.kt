package com.ritikshaw.kolinmvvm.activity.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ritikshaw.kolinmvvm.activity.model.UserData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class ProfileRepository(
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
) {

    suspend fun getUserFromFirebase(uId : String): Result<UserData>{
        return suspendCancellableCoroutine { continuation->
            val userRef = firebaseDatabase.getReference("userDetails")
            val valueListener = object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userData = snapshot.child(uId).getValue(UserData::class.java)
                    if (userData!=null){
                        if (continuation.isActive){
                            continuation.resume(Result.success(userData))
                        }
                    }
                    else{
                        if (continuation.isActive){
                            continuation.resume(Result.failure(Exception("User not found")))
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive){
                        continuation.resume(Result.failure(Exception(error.message)))
                    }
                }
            }

            userRef.addValueEventListener(valueListener)
            continuation.invokeOnCancellation {
                userRef.removeEventListener(valueListener)
            }
        }
    }
}