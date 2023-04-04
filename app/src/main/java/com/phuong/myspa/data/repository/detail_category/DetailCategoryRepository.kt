package com.phuong.myspa.data.repository.detail_category

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.FavoriteDT0
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class DetailCategoryRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun getShopsByCategory(params: QueryCategory, page: String): ApiResponse<DataShop>
    ?{
        return try {
            withContext(dispatcher){
                provideRemoteAPI.getShopsByCategory(params,page)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }

}