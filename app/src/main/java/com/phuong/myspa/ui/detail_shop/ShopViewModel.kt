package com.phuong.myspa.ui.detail_shop

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopService
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.shop.ShopRepository
import com.phuong.myspa.data.repository.shop.ShopUseCase
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                         private val shopUseCase: ShopUseCase,private val shopRepository: ShopRepository)
    : BaseLoadingDataViewModel<ApiResponse<Shop>>() {
    var isSucess = MutableLiveData<Boolean>()
    var isReport = MutableLiveData<Boolean>()
    var serviceLiveData = MutableLiveData<DataResponse<ShopService>>(DataResponse.DataIdle())
    fun getDetailShop(shopId: String) {
        viewModelScope.launch(dispatcher) {
            shopUseCase.invoke(shopId).collect {
                dataMutableLiveData.postValue(it)
            }
        }
    }
    fun getDetailService(serviceId:String){
        viewModelScope.launch(dispatcher) {
            val response =   shopRepository.getDetailService(serviceId)
            if (response?.success == true){
                response.data?.avatar =  Constants.BASE_URL + response.data?.avatar?.replace("\\", "/")

                serviceLiveData.postValue(DataResponse.DataSuccess(response.data!!))
            }
            else{ serviceLiveData.postValue(DataResponse.DataError())
            }
        }
    }
    fun addCart(card: CartDTO){
        viewModelScope.launch (dispatcher){
            val response =   shopRepository.addCart(card)?.success
            if (response == true){
            isSucess.postValue(true)
            }
            else{ isSucess.postValue(false)
            }
        }
    }
    fun postReport(report:UploadComment){
        viewModelScope.launch (dispatcher){
            val response = shopRepository.postReport(report)
            if (response?.success == true){
                isReport.postValue(true)
            }
            else{
                isReport.postValue(false)
            }
        }
    }
}