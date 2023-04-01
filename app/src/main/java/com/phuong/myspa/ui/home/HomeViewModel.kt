package com.phuong.myspa.ui.home

import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.repository.home.HomeUseCase
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val homeUseCase: HomeUseCase)
    : BaseLoadingDataViewModel<ApiResponse<MutableList<Category>>>(){

    fun getListCategory(){
        viewModelScope.launch(dispatcher) {
            homeUseCase.invoke(Any()).collect{
                dataMutableLiveData.postValue(it)
            }
        }
    }


}