package com.phuong.myspa.data.repository.favorite

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.FavoriteDT0
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.net.UnknownHostException
import javax.inject.Inject

class FavoriteRepository  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                              private val provideRemoteAPI: RemoteServices,@AppContext private val app: MyApp
) {
    suspend fun addFavorite(idShop:String): ApiResponse<Any>
    ?{
        return try {
             withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.addFavorite(token, FavoriteDT0((idShop)))
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }

    suspend fun deleteFavorite(idShop:String): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.deleteFavorite(token,FavoriteDT0((idShop)))
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun getListFavorite(): ApiResponse<MutableList<ShopInfor>>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.getListFavorite(token)
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