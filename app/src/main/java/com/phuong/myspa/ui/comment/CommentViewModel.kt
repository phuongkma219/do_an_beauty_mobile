package com.phuong.myspa.ui.comment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.comment.Comment
import com.phuong.myspa.data.api.model.comment.DataComment
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.shop.DataShop
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.comment.CommentRepository
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CommentViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher
                                           , private val commentRepository : CommentRepository,
):BaseLoadingDataViewModel<MutableList<Comment>>() {
    var dataVM : DataComment? = null
    var isComment = MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())
    fun fetchData(id:String,isLoadMore:Boolean){
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
                            if (dataVM!!.comments != null) {
                                getPage(id)
                            } else {
                                0
                            }
                        }
                    } else {
                        0
                    }
                val responseData = commentRepository.getComments(id,requestPage.toString())
                if (responseData?.success == true){
                    val comments = responseData.data?.comments
                    if (comments?.size != 0 ){
                        dataVM = responseData.data
                        comments?.forEach{ sh ->
                            sh.user?.avatar = Constants.BASE_URL +  sh.user?.avatar?.replace("\\", "/")
                        }
                        dataMutableLiveData.postValue(DataResponse.DataSuccess(responseData.data!!.comments!!))
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
    fun getPage(id:String):Int {
        return dataVM!!.next_page.replace("comments_shop/$id?page=", "").toInt()
    }
    fun uploadComment(content:UploadComment){
        isComment.value!!.loadingStatus = LoadingStatus.Loading
        viewModelScope.launch(dispatcher) {
          val response =  commentRepository.uploadComment(content)
            if (response?.success == true){
                isComment.postValue(DataResponse.DataSuccess(true))
            }else{
                isComment.postValue(DataResponse.DataError())
            }

        }
    }
}