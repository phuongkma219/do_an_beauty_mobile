package com.phuong.myspa.ui.login

import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.repository.signUp.SignUpUseCase
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel  @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val signUpUseCase: SignUpUseCase)
    : BaseLoadingDataViewModel<ApiResponse<Any>>(){

    fun userSignUp(userSignUp: UserSignUp){
        viewModelScope.launch(dispatcher) {
            signUpUseCase.invoke(userSignUp).collect{
                dataMutableLiveData.postValue(it)
            }
        }
    }


}