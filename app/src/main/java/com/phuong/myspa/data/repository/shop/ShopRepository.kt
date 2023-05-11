package com.phuong.myspa.data.repository.shop

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.AddCart
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class ShopRepository  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                          private val provideRemoteAPI: RemoteServices,
                                          @AppContext private val app: MyApp) {
    suspend fun getDetailShop(shopId: String): ApiResponse<Shop>
    ?{
        return try {
             withContext(dispatcher){
                provideRemoteAPI.getDetailShop(shopId)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun addCart(addCart: AddCart): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.addCart(token,addCart)
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