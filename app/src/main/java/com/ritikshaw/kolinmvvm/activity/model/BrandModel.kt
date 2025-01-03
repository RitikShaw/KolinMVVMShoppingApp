package com.ritikshaw.kolinmvvm.activity.model

import com.google.gson.annotations.SerializedName

data class BrandModel(
    @SerializedName("id")
    val id : Int = 0,
    @SerializedName("picUrl")
    val picUrl :String = "",
    @SerializedName("price")
    val price : Int = 0,
    @SerializedName("title")
    val title : String = ""
)
