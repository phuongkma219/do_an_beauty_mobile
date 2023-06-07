package com.phuong.myspa.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.cart.DataCart
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.model.shop.DataModel
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.cart.CartRepository
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
, private val cartRepository: CartRepository ):BaseLoadingDataViewModel<MutableList<DataCart>?>(){
    var isDelete= MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())
    var liveDataAddHistory= MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())

    fun getListCart(){
       viewModelScope.launch (dispatcher){
           dataMutableLiveData.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
        val response =  cartRepository.getListCart()
           if (response?.success == true){
               var list = response.data
               list?.forEach{ sh ->
                   sh.items.forEach {
                       it.serviceDetail.avatar = Constants.BASE_URL +   it.serviceDetail.avatar?.replace("\\", "/")
                   }
               }
               dataMutableLiveData.postValue(DataResponse.DataSuccess(list))
           }
           else{
               dataMutableLiveData.postValue(DataResponse.DataError())

           }
       }
    }
    fun deleteCart(cartDTO: CartDTO){
        viewModelScope.launch (dispatcher){
            val response = cartRepository.deteleCart(cartDTO)
            if (response?.success == true){
                isDelete.postValue(DataResponse.DataSuccess(true))
                getListCart()
            }
            else{
                isDelete.postValue(DataResponse.DataError())
            }
        }
    }
    fun deleteListCart(data: MutableList<DataModel.DataItem>){
        isDelete.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
        try {
            data.forEach {
                viewModelScope.launch (dispatcher){
                    cartRepository.deteleCart(CartDTO(it.shop_id,it._id))
                }
            }
            isDelete.postValue(DataResponse.DataLoading(LoadingStatus.AddHistory))
        }
        catch (e:Exception){
            isDelete.postValue(DataResponse.DataError())

        }
        getListCart()


    }
    fun addHistory(data:MutableList<DataModel.DataItem>) {
        liveDataAddHistory.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
       try {
           data.forEach {
               viewModelScope.launch (dispatcher){
                   val response = cartRepository.addHistory(HistoryDTO(it.shop_id,it._id))
               }
           }
           liveDataAddHistory.postValue(DataResponse.DataSuccess(true))

       }
       catch (e:Exception){
           liveDataAddHistory.postValue(DataResponse.DataError())

       }

    }

}