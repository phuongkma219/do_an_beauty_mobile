package com.phuong.myspa.ui.login

import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.login.LoginUseCase
import com.phuong.soundeditor23.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val loginUseCase: LoginUseCase)
    :BaseLoadingDataViewModel<UserLogin>(){

    fun loginUser(userDTO: UserDTO){
        viewModelScope.launch(dispatcher) {
            loginUseCase.invoke(userDTO).collect{
                if (it.loadingStatus == LoadingStatus.Success){
                val data =    it as DataResponse.DataSuccess
                }
              dataMutableLiveData.postValue(it)
            }
        }
    }


}