package com.phuong.myspa.data.repository.cart

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.cart.DataCart
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class CartRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                         @AppContext private val app: MyApp,
                                         private val provideRemoteAPI: RemoteServices
) {

    suspend fun getListCart(): ApiResponse<MutableList<DataCart>>?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.getListCart(token)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun getListHistory(): ApiResponse<MutableList<History>>?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.getListHistory(token)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun deteleCart(cartDTO: CartDTO): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.deleteCart(token,cartDTO)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun addHistory(historyDTO: HistoryDTO): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.addHistory(token,historyDTO)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun deleteHistory(historyDTO: HistoryDTO): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.deleteHistory(token,historyDTO)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun getHistoryDetail(id:String, historyDTO: HistoryDTO): ApiResponse<History?>
    ? {
        return   try {

             withContext(dispatcher) {
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app)
                    .getData()!!.access_token
                provideRemoteAPI.getDetailHistory(
                    token,
                    id,
                    historyDTO.shop_id,
                    historyDTO.service_id
                )
            }

        } catch (ex: UnknownHostException) {
            null
        } catch (ex: Exception) {
            null
        }
    }
}