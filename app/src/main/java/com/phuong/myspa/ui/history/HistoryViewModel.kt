package com.phuong.myspa.ui.history

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.MyApp
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.cart.CartDTO
import com.phuong.myspa.data.api.model.cart.DataCart
import com.phuong.myspa.data.api.model.history.History
import com.phuong.myspa.data.api.model.history.HistoryDTO
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.cart.CartRepository
import com.phuong.myspa.data.repository.home.HomeUseCase
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                        private val cartRepository: CartRepository ,
)
    : BaseLoadingDataViewModel<MutableList<History>?>(){
    var isDelete= MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())

    fun getListHistory(){
        viewModelScope.launch(dispatcher) {
            viewModelScope.launch (dispatcher){
                val response =  cartRepository.getListHistory()
                if (response?.success == true){
                    var list = response.data
                    list?.forEach{ sh ->
                            sh.service?.avatar = Constants.BASE_URL +   sh.service.avatar?.replace("\\", "/")
                        }
                    dataMutableLiveData.postValue(DataResponse.DataSuccess(list))

                }

                else{
                    dataMutableLiveData.postValue(DataResponse.DataError())

                }

            }
        }
    }
    fun deleteHistory(historyDTO: HistoryDTO){
        viewModelScope.launch (dispatcher){
            val response = cartRepository.deleteHistory(historyDTO)
            if (response?.success == true){
                isDelete.postValue(DataResponse.DataSuccess(true))
                getListHistory()
            }
            else{
                isDelete.postValue(DataResponse.DataError())
            }
        }
    }


}