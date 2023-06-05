package com.phuong.myspa.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.MyApp
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.repository.home.HomeRepository
import com.phuong.myspa.data.repository.home.HomeUseCase
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.CacheUtils
import com.phuong.myspa.utils.SharedPreferenceUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher,
                                       private val homeRepository: HomeRepository,
                                        private val homeUseCase: HomeUseCase,@AppContext private val app: MyApp
)
    : BaseLoadingDataViewModel<ApiResponse<MutableList<Category>>>(){
     var countCart = MutableLiveData<Int>()

    fun getListCategory(){
        viewModelScope.launch(dispatcher) {
            homeUseCase.invoke(Any()).collect{
                if (it is DataResponse.DataError){
                    val data = CacheUtils.readSaveCache(app)
                    if (data != null){ dataMutableLiveData.postValue(DataResponse.DataSuccess(data!!))
                    }
                    else{
                        dataMutableLiveData.postValue(DataResponse.DataError())
                    }
                }
                else {
                    if(it is DataResponse.DataSuccess){
                        CacheUtils.writeSaveCache(app,(it as DataResponse.DataSuccess).body)
                    }
                    dataMutableLiveData.postValue(it)
                }
            }
        }
        getCountCart()
    }
    fun getCountCart(){
        viewModelScope.launch (dispatcher){
            val response = homeRepository.getCountCart()
            if (response?.success == true){
                countCart.postValue(response.data)
            }
            else{
                countCart.postValue(0)
            }
        }
    }


}