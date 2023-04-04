package com.phuong.myspa.data.repository.home

import com.phuong.myspa.MyApp
import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.di.AppContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase  @Inject constructor(private val homeRepository: HomeRepository):
    BaseUseCase<Any, ApiResponse<MutableList<Category>>>() {
    override suspend fun execute(param: Any): Flow<DataResponse< ApiResponse<MutableList<Category>>>> = flow{
        val data = homeRepository.getListCategory()
        if (data?.success == true) {
            data.data?.forEach {
                it.avatar = Constants.BASE_URL + it.avatar.replace("\\", "/")
            }
            emit(DataResponse.DataSuccess(data))
        } else {
                emit(DataResponse.DataError())
        }
    }
}