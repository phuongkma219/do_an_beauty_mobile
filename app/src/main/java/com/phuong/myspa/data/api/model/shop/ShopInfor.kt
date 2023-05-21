package com.phuong.myspa.data.api.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopInfor(
    var __v: Int,
    var _id: String,
    var address: String,
    var avatar: String,
    var category: List<String>,
    var created_at: String,
    var description: String,
    var email: String,
    var end_time: String,
    var name: String,
    var phone_number: String,
    var rate: Double,
    var start_time: String,
    var updated_at: String
):Parcelable,DataModel()