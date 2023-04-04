package com.phuong.myspa.data.api.model.remote

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.io.Serializable


class ApiResponse<T> : Serializable {
    var success: Boolean? = null
    var message: String? = null
    var data: T? = null
    var statusCode = 0

}