package com.phuong.myspa.data.repository.signUp

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class SignUpRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun userRegister(userSignUp: UserSignUp): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                provideRemoteAPI.userRegister(userSignUp)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }

}