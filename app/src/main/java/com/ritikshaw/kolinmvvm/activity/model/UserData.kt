package com.ritikshaw.kolinmvvm.activity.model

import java.io.Serializable

data class UserData(
    val userId : String="",
    val name : String="",
    val email : String="",
    val phoneNumber : String="",
    val profilePicture: String="",
    val password : String=""
) : Serializable {
    override fun toString(): String {
        return name
    }
}
