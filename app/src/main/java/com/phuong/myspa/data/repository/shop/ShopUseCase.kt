package com.phuong.myspa.data.repository.shop

import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShopUseCase  @Inject constructor(private val shopRepository: ShopRepository):
    BaseUseCase<String, ApiResponse<Shop>>() {
    override suspend fun execute(param: String): Flow<DataResponse<ApiResponse<Shop>>> = flow{
        val data = shopRepository.getDetailShop(param)
        if (data.success == true) {
            data.data?.infor?.avatar =  Constants.BASE_URL + data.data?.infor?.avatar?.replace("\\", "/")
            data.data?.service?.forEach {
               it.avatar =  Constants.BASE_URL + it.avatar.replace("\\", "/")
            }
            emit(DataResponse.DataSuccess(data))
        } else {
            emit(DataResponse.DataError())
        }
    }
}