package com.phuong.myspa.ui.detail_category

import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.QueryCategory
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.detail_category.DetailCategoryRepository
import com.phuong.myspa.utils.Constants
import com.phuong.soundeditor23.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailCategoryViewModel@Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
,private val detailCategoryRepository: DetailCategoryRepository) :BaseLoadingDataViewModel<MutableList<ShopInfor>>() {
    var dataVM : DataShop? = null
    fun fetchData(params:QueryCategory,isLoadMore:Boolean){
        if (dataMutableLiveData.value!!.loadingStatus != LoadingStatus.Loading && dataMutableLiveData.value!!.loadingStatus != LoadingStatus.LoadingMore
            && dataMutableLiveData.value!!.loadingStatus != LoadingStatus.Refresh){
            if (!isLoadMore) {
                if (dataVM == null) {
                    dataMutableLiveData.value!!.loadingStatus = LoadingStatus.Loading
                } else {
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
                            if (dataVM!!.data.isNotEmpty()) {
                                dataVM!!.next_page
                            } else {
                                0
                            }
                        }
                    } else {
                        0
                    }
                val responseData = detailCategoryRepository.getShopsByCategory(params,requestPage.toString())
                if (responseData.success == true){
                    dataVM = responseData.data
                    val shops = responseData.data!!.list_shop
                    shops.forEach{ sh ->
                        sh.avatar = Constants.BASE_URL +  sh.avatar.replace("\\", "/")
                    }
                    dataMutableLiveData.postValue(DataResponse.DataSuccess(responseData.data!!.list_shop))
                }
                else{
                    dataMutableLiveData.postValue(DataResponse.DataError())
                }
            }
        }
    }
}