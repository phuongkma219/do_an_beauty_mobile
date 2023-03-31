package com.phuong.myspa.data.api.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopInfor(
    val __v: Int,
    val _id: String,
    val address: String,
    var avatar: String,
    val category: List<String>,
    val created_at: String,
    val description: String,
    val email: String,
    val end_time: String,
    val name: String,
    val phone_number: String,
    val rate: Double,
    val start_time: String,
    val updated_at: String
):Parcelable