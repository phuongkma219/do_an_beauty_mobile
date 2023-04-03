package com.phuong.myspa.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.favorite.FavoriteRepository
import com.phuong.myspa.data.repository.shop.ShopUseCase
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val favoriteRepository: FavoriteRepository)
    : BaseLoadingDataViewModel<ApiResponse<Shop>>()  {

        var isSuccess = MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())
    fun addFavorite(shopId : String){
        viewModelScope.launch (dispatcher){
           val data = favoriteRepository.addFavorite(shopId)
            if (data.success == true){
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
            if (data.success == true){
                isSuccess.postValue(DataResponse.DataSuccess(true))
            }
            else{
                isSuccess.postValue(DataResponse.DataSuccess(false))
            }
        }
    }
}