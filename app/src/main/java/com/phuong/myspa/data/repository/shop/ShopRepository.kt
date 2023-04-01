package com.phuong.myspa.data.repository.shop

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopRepository  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun getDetailShop(shopId: String): ApiResponse<Shop> = withContext(dispatcher){
        provideRemoteAPI.getDetailShop(shopId)
    }
}