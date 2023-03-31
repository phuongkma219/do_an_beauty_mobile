package com.phuong.myspa.data.api.model.remote

class ApiResponse<T> {
    var success: Boolean? = null
    var message: String? = null
    var data: T? = null
    var statusCode = 0

}