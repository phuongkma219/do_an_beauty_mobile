package com.phuong.myspa.ui.detail_shop

import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.repository.shop.ShopUseCase
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val shopUseCase: ShopUseCase)
    : BaseLoadingDataViewModel<ApiResponse<Shop>>() {

    fun getDetailShop(shopId: String) {
        viewModelScope.launch(dispatcher) {
            shopUseCase.invoke(shopId).collect {
                dataMutableLiveData.postValue(it)
            }
        }
    }
}