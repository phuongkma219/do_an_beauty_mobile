package com.phuong.myspa.ui.detail_shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.AddCart
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.repository.shop.ShopRepository
import com.phuong.myspa.data.repository.shop.ShopUseCase
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                         private val shopUseCase: ShopUseCase,private val shopRepository: ShopRepository)
    : BaseLoadingDataViewModel<ApiResponse<Shop>>() {
    var isSucess = MutableLiveData(false)
    fun getDetailShop(shopId: String) {
        viewModelScope.launch(dispatcher) {
            shopUseCase.invoke(shopId).collect {
                dataMutableLiveData.postValue(it)
            }
        }
    }
    fun addCart(card:AddCart){
        viewModelScope.launch (dispatcher){
            val response =   shopRepository.addCart(card)?.success
            if (response == true){
            isSucess.postValue(true)
            }
            else{ isSucess.postValue(response)
            }
        }
    }
}