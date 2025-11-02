package com.ritikshaw.kolinmvvm.activity.model

import java.io.Serializable

data class ItemData(
    val id : String = " ",
    val description: String=" ",
    val picUrl: List<String> = emptyList(),
    val price: Double = 0.00,
    val rating: Double = 0.00,
    val size: List<String> = emptyList(),
    val title: String  = " "
):Serializable