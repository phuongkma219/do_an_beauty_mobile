package com.phuong.myspa.data.api.response

sealed class DataResponse<T> constructor(var loadingStatus: LoadingStatus) {
    class DataLoading<T>(private val loadingType : LoadingStatus) : DataResponse<T>(loadingType)
    class DataIdle<T> : DataResponse<T>(LoadingStatus.Idle)
    class DataError<T>(val body: T? = null) : DataResponse<T>(LoadingStatus.Error)
    data class DataSuccess<T>(val body: T) : DataResponse<T>(LoadingStatus.Success)
        class DataErrorResponse<T>(val throwable: Exception?) : DataResponse<T>(LoadingStatus.Error)
}