package com.phuong.myspa.data.repository.signUp

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.soundeditor23.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun userRegister(userSignUp: UserSignUp): ApiResponse<Any> = withContext(dispatcher){
        provideRemoteAPI.userRegister(userSignUp)
    }
}