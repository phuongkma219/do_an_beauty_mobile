package com.phuong.myspa.data.repository.login

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.login.UserDTO
import com.phuong.myspa.data.api.model.login.UserLogin
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun loginUser(userDTO: UserDTO): ApiResponse<UserLogin> = withContext(dispatcher){
            provideRemoteAPI.userLogin(userDTO)
    }
}