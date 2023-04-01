package com.phuong.myspa.data.repository.home

import com.phuong.myspa.MyApp
import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.Constants
import com.phuong.soundeditor23.di.AppContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeUseCase  @Inject constructor(private val homeRepository: HomeRepository,@AppContext private val app: MyApp):
    BaseUseCase<Any, ApiResponse<MutableList<Category>>>() {
    override suspend fun execute(param: Any): Flow<DataResponse< ApiResponse<MutableList<Category>>>> = flow{
        val data = homeRepository.getListCategory()
        if (data.success == true) {
            data.data?.forEach {
                it.avatar = Constants.BASE_URL + it.avatar.replace("\\", "/")
            }
            CacheUtils.writeSaveCache(app,data)
            emit(DataResponse.DataSuccess(data))
        } else {
            val dataLocal = CacheUtils.readSaveCache(app)
            if (dataLocal!= null){
                emit(DataResponse.DataSuccess(dataLocal))
            }else{
                emit(DataResponse.DataError())
            }
        }
    }
}