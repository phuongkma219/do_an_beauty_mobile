package com.phuong.myspa.utils

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import dagger.hilt.android.lifecycle.HiltViewModel

class ShareViewModel( app: Application): ViewModel() {
   private val loadSetting = MutableLiveData<LoadingStatus>()
    val get: LiveData<LoadingStatus> = loadSetting
     fun setLoadSetting(loadingStatus: LoadingStatus){
        loadSetting.postValue(loadingStatus)
    }



    companion object: SingletonHolder<ShareViewModel, Application>(::ShareViewModel)

}