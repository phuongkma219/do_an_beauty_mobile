package com.phuong.myspa.ui.detail_category

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.detail_category.DetailCategoryRepository
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCategoryViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
,private val detailCategoryRepository: DetailCategoryRepository) :BaseLoadingDataViewModel<MutableList<ShopInfor>>() {
    var dataVM : DataShop? = null
    fun fetchData(params:QueryCategory,isLoadMore:Boolean){
        if (dataMutableLiveData.value!!.loadingStatus != LoadingStatus.Loading && dataMutableLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
            && dataMutableLiveData.value!!.loadingStatus != LoadingStatus.Refresh){
            if (!isLoadMore) {
                if (dataVM == null) {
                    dataMutableLiveData.value!!.loadingStatus = LoadingStatus.Loading
                } else {
                    dataVM = null
                    dataMutableLiveData.value!!.loadingStatus = LoadingStatus.Refresh
                }
            } else {
                dataMutableLiveData.value = DataResponse.DataLoading(LoadingStatus.LoadingMore)
            }
            viewModelScope.launch (dispatcher){
                val requestPage =
                    if (dataVM != null) {
                        if (dataMutableLiveData.value!!.loadingStatus == LoadingStatus.Refresh) {
                            0
                        } else {
                            if (dataVM!!.shops != null) {
                               getPage()
                            } else {
                                0
                            }
                        }
                    } else {
                        0
                    }
                val responseData = detailCategoryRepository.getShopsByCategory(params,requestPage.toString())
                if (responseData?.success == true){
                    val shops = responseData.data?.shops
                    if (shops?.size != 0 ){
                        dataVM = responseData.data
                        shops?.forEach{ sh ->
                        sh.avatar = Constants.BASE_URL +  sh.avatar.replace("\\", "/")
                    }
                        dataMutableLiveData.postValue(DataResponse.DataSuccess(responseData.data!!.shops!!))
                    }
                    else{
                        dataMutableLiveData.postValue(DataResponse.DataIdle())
                        dataVM = null
                    }

                }
                else{
                    dataMutableLiveData.postValue(DataResponse.DataError())

                }

            }
        }
    }
    fun getPage():Int {
       return dataVM!!.next_page.replace("get_list?page=", "").toInt()
    }
}