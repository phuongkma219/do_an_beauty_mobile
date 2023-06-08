package com.phuong.myspa.ui.history

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.cart.CartRepository
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailHistoryViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                                 private val cartRepository: CartRepository )
    : BaseLoadingDataViewModel<History>(){

    fun getHistoryDetail(id:String,historyDTO: HistoryDTO){
        dataMutableLiveData.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
        viewModelScope.launch(dispatcher){
           val response =  cartRepository.getHistoryDetail(id, historyDTO)
            if (response?.success ==true){
                val data = response.data!!
                data.service.avatar = Constants.BASE_URL +   data.service.avatar?.replace("\\", "/")

                dataMutableLiveData.postValue(DataResponse.DataSuccess(data))
            }
            else{
                dataMutableLiveData.postValue(DataResponse.DataError())
            }
        }
    }
}