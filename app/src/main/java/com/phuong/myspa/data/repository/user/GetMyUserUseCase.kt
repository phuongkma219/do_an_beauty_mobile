package com.phuong.myspa.data.repository.user

import com.phuong.myspa.base.BaseUseCase
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.shop.ShopRepository
import com.phuong.myspa.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMyUserUseCase  @Inject constructor(private val userRepository: UserRepository):
    BaseUseCase<Any, ApiResponse<User>>() {
    override suspend fun execute(param: Any): Flow<DataResponse<ApiResponse<User>>> = flow{
        val data = userRepository.getMyUser()
        if (data.success == true) {
            data.data?.avatar =  Constants.BASE_URL + data.data?.avatar?.replace("\\", "/")
            emit(DataResponse.DataSuccess(data))
        } else {
            emit(DataResponse.DataError())
        }
    }
}