package com.phuong.myspa.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.search.DataSearch
import com.phuong.myspa.data.api.model.search.Search
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.search.SearchRepository
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
                                           , private val searchRepository: SearchRepository
) : BaseLoadingDataViewModel<MutableList<Search>?>() {
    var dataVM : DataSearch? = null
    private var keyword :String? =""
    private var page =0
    fun setKeyword(key: String?){
        keyword = key
    }

    override val isEmpty: LiveData<Boolean>
        get() = Transformations.map(dataLiveData) {
            it.loadingStatus == LoadingStatus.Error ||
                    (  it.loadingStatus == LoadingStatus.Success && (dataLiveData.value as DataResponse.DataSuccess).body == null)

        }
    fun fetchData(isLoadMore:Boolean){
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
                Log.d("kkk", "123123: $dataVM")
                val requestPage =
                    if (dataVM != null) {
                        if (dataMutableLiveData.value!!.loadingStatus == LoadingStatus.Refresh) {
                            0
                        } else {
                            if (dataVM!!.list_shop?.size!!>0) {
                                page += 1
                                page
                            } else {
                                0
                            }
                        }
                    } else {
                        0
                    }
                val responseData = searchRepository.searchShops(keyword,requestPage.toString())
                if (responseData?.success == true){
                    val shops = responseData.data?.list_shop
                    if (shops != null){
                        dataVM = responseData.data
                        shops?.forEach{ sh ->
                            sh.shop.avatar = Constants.BASE_URL +  sh.shop.avatar.replace("\\", "/")
                        }
                        dataMutableLiveData.postValue(DataResponse.DataSuccess(responseData.data!!.list_shop))
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
        try {
            //get_list?keyword=Hà?page=3
//            return dataVM!!.next_page.replace("get_list?keyword=$keyword?page=", "").toInt()
        return page
        }
        catch (e:Exception){
            e.printStackTrace()
            return 0
        }
    }
}