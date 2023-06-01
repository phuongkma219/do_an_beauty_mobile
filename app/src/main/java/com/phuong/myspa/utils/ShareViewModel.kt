package com.phuong.myspa.utils

import android.app.Application
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.phuong.myspa.data.api.response.DataResponse
import dagger.hilt.android.lifecycle.HiltViewModel

class ShareViewModel( app: Application): ViewModel() {
    private val _intent = MutableLiveData<Intent>()

    val get: LiveData<Intent> = Transformations.map(_intent) { it!! }

    fun set(intent: Intent) { _intent.value = intent }
    private val _message = MutableLiveData<DataResponse<String>>(DataResponse.DataIdle())

    val getM: LiveData<DataResponse<String>> = Transformations.map(_message) { it!! }

    fun setSucces(mess: String) { _message.postValue(DataResponse.DataSuccess(mess)) }
    fun setErr(mess: String) { _message.postValue(DataResponse.DataError(mess)) }


    companion object: SingletonHolder<ShareViewModel, Application>(::ShareViewModel)

}