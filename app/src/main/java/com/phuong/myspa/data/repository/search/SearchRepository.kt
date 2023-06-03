package com.phuong.myspa.data.repository.search

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.search.DataSearch
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class SearchRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun searchShops(keyword: String?,page:String): ApiResponse<DataSearch>
    ?{
        return try {
            withContext(dispatcher){
                provideRemoteAPI.searchShops(keyword,page)
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