package com.phuong.soundeditor23.base

import com.phuong.myspa.data.api.response.LoadingStatus


sealed class BaseResponse<out R>(var loadingStatus: LoadingStatus) {
    object Loading : BaseResponse<Nothing>(LoadingStatus.Loading)
    object Idle : BaseResponse<Nothing>(LoadingStatus.Idle)
    data class Success<out T>(val data: T) : BaseResponse<T>(LoadingStatus.Success)
    data class Error(val code: Int? = null, val msg: String? = "") : BaseResponse<Nothing>(
        LoadingStatus.Error)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[code=${code} msg=$msg]"
            else -> "Loading"
        }
    }
}

val <T> BaseResponse<T>.data: T?
    get() = (this as? BaseResponse.Success)?.data
