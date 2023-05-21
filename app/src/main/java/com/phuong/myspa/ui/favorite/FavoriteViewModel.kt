package com.phuong.myspa.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.favorite.FavoriteRepository
import com.phuong.myspa.data.repository.shop.ShopUseCase
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val favoriteRepository: FavoriteRepository)
    : BaseLoadingDataViewModel<MutableList<ShopInfor>?>()  {

        var isSuccess = MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())
    fun addFavorite(shopId : String,isFavorite : Boolean){
        viewModelScope.launch (dispatcher){
           val data = if (isFavorite){
               favoriteRepository.deleteFavorite(shopId)
           }
            else{
                favoriteRepository.addFavorite(shopId)
           }
            if (data?.success == true && !isFavorite){
                isSuccess.postValue(DataResponse.DataSuccess(true))
            }
            else{
                isSuccess.postValue(DataResponse.DataSuccess(false))
            }
        }
    }
    fun deleteFavorite(shopId : String){
        viewModelScope.launch (dispatcher){
            val data = favoriteRepository.deleteFavorite(shopId)
            if (data?.success == true){
                isSuccess.postValue(DataResponse.DataSuccess(true))
            }
            else{
                isSuccess.postValue(DataResponse.DataSuccess(false))
            }
        }
    }
    fun getListFavorite(){
        viewModelScope.launch (dispatcher){
            dataMutableLiveData.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
            val data = favoriteRepository.getListFavorite()
            if (data?.success == true){
                data.data.let {
                    it?.forEach {
                    it?.avatar =  Constants.BASE_URL + it?.avatar?.replace("\\", "/")
                }
                }
                if (data.data == null){
                    dataMutableLiveData.postValue(DataResponse.DataError())
                }
                else{
                    dataMutableLiveData.postValue(DataResponse.DataSuccess(data.data))
                }
            }
            else{
                dataMutableLiveData.postValue(DataResponse.DataError())
            }
        }
    }
}