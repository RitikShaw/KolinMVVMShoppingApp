package com.ritikshaw.kolinmvvm.activity.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ritikshaw.kolinmvvm.activity.model.UserData
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume


class AuthenticationRepository(
    private val firebasthAuth : FirebaseAuth = FirebaseAuth.getInstance()

) {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    suspend fun registerWithEmail(email : String, password : String): Result<String>{
        return try {
            val result =firebasthAuth.createUserWithEmailAndPassword(email,password).await()
            val user = result.user
            if (user!=null){
                Result.success(user.uid)
            }
            else{
                Result.failure(Exception("User is null"))
            }
        }
        catch (e: Exception){
            Result.failure(e)
        }
    }

    suspend fun loginWithEmail(email: String, password: String): Result<String> {
        return try {
            val result = firebasthAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            if (user!=null){
                Result.success(user.uid)
            }
            else{
                Result.failure(Exception("User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken : String): Result<Any> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebasthAuth.signInWithCredential(credential).await()
            val user = result.user
            val isNewUSer = result.additionalUserInfo?.isNewUser
            if (user!=null){
                Result.success(user)
            }
            else{
                Result.failure(Exception("User is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserIntoFirebase(userData : UserData): Result<String> {

        return try {
            val userRef = firebaseDatabase.getReference("userDetails").child(userData.userId)
            userRef.setValue(userData).await()
            Result.success(userData.userId)
        }
        catch (e : Exception){
            Result.failure(e)
        }

    }

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