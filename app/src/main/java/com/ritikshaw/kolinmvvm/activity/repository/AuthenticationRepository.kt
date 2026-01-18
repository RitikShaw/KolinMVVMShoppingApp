package com.ritikshaw.kolinmvvm.activity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ritikshaw.kolinmvvm.activity.model.UserData
import kotlinx.coroutines.tasks.await


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

    suspend fun signInWithGoogle(idToken : String): Result<Pair<FirebaseUser, Boolean>> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = firebasthAuth.signInWithCredential(credential).await()
            val user = result.user
            val isNewUser = result.additionalUserInfo?.isNewUser
            if (user!=null){
                Result.success(Pair(user,isNewUser?:false))
            }
            else{
                Result.failure(Exception("User is null"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
    suspend fun createPassword(user: FirebaseUser, password: String): Result<UserData> {
        return try {
            val email = user.email
                ?: return Result.failure(Exception("User email not found"))

            val credential = EmailAuthProvider.getCredential(email, password)

            user.linkWithCredential(credential).await()

            Result.success(
                UserData(
                    userId = user.uid,
                    email = email,
                    password = password,
                    name = user.displayName?:"New User"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUserIntoFirebase(userData : UserData): Result<String> {

        return try {
            val userRef = firebaseDatabase.getReference("userDetails").child(userData.userId)
            val snapshot = userRef.get().await()
            if (snapshot.exists()){
                Result.failure(Exception("User already exists"))
            }
            else{
                userRef.setValue(userData).await()
                Result.success(userData.userId)
            }
        }
        catch (e : Exception){
            Result.failure(e)
        }
    }

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
}