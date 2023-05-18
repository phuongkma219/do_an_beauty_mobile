package com.phuong.myspa.ui.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.DataCart
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.cart.CartRepository
import com.phuong.myspa.data.repository.comment.CommentRepository
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
, private val cartRepository: CartRepository ):ViewModel(){
    var listCart = MutableLiveData<DataResponse<MutableList<DataCart>?>>(DataResponse.DataIdle())
    fun getListCart(){
       viewModelScope.launch (dispatcher){
        val response =  cartRepository.getListCart()
           if (response?.success == true){
               var list = response.data
               list?.forEach{ sh ->
                   sh.items.forEach {
                       it.serviceDetail.avatar = Constants.BASE_URL +   it.serviceDetail.avatar?.replace("\\", "/")
                   }


               }
               listCart.postValue(DataResponse.DataSuccess(list))
           }
           else{
               listCart.postValue(DataResponse.DataError())

           }
       }
    }
}