package com.phuong.myspa.data.api.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopService(
    val __v: Int,
    val _id: String,
    var avatar: String,
    val created_at: String,
    val description: String,
    val name: String,
    val price: String,
    val rate: Int,
    val shop_id: String,
    val time: String,
    val updated_at: String
):Parcelable,DataModel()