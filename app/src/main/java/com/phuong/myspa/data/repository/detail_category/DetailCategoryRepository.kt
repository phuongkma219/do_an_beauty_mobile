package com.phuong.myspa.data.repository.detail_category

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.soundeditor23.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.http.Query
import javax.inject.Inject

class DetailCategoryRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun getShopsByCategory(params: QueryCategory, page: String): ApiResponse<DataShop> = withContext(dispatcher){
        provideRemoteAPI.getShopsByCategory(params,page)
    }
}