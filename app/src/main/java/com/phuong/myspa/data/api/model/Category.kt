package com.phuong.myspa.data.api.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

data class Category(
    val __v: Int,
    val _id: String,
    var avatar: String,
    val created_at: String,
    val name: String,
    val updated_at: String
): Serializable
