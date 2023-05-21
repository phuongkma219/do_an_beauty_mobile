package com.phuong.myspa.data.repository.home

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class HomeRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                         @AppContext private val app: MyApp,
                                         private val provideRemoteAPI: RemoteServices) {
    suspend fun getListCategory(): ApiResponse<MutableList<Category>> ?{
        return try {
            withContext(dispatcher){
                provideRemoteAPI.getListCategory()
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