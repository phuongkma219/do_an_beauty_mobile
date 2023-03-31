package com.phuong.myspa.base

import android.util.Log
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

abstract class BaseUseCase<in P, R> constructor(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {

    protected abstract suspend fun execute(param: P): Flow<DataResponse<R>>

    suspend fun invoke(param: P): Flow<DataResponse<R>> = execute(param).onStart {
        emit(DataResponse.DataLoading(LoadingStatus.Loading))
    }.catch { exception ->
        emit(DataResponse.DataError())
    }.flowOn(dispatcher)
}