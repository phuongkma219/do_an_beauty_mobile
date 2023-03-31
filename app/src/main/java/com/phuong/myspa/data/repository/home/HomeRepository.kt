package com.phuong.myspa.data.repository.home

import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.Category
import com.phuong.myspa.data.api.model.login.UserSignUp
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.soundeditor23.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices) {
    suspend fun getListCategory(): ApiResponse<MutableList<Category>> = withContext(dispatcher){
        provideRemoteAPI.getListCategory()
    }
}