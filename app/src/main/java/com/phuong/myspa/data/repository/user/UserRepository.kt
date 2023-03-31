package com.phuong.myspa.data.repository.user

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.shop.Shop
import com.phuong.myspa.data.api.model.user.ImageUpload
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.soundeditor23.di.AppContext
import com.phuong.soundeditor23.di.DefaultDispatcher
import com.phuong.soundeditor23.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor(@IoDispatcher
private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices,@AppContext private val app: MyApp) {
    suspend fun getMyUser(): ApiResponse<User> = withContext(dispatcher){
        val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
        provideRemoteAPI.getMyUser(token)
    }
    suspend fun uploadAvatar(idUser:String,image: MultipartBody.Part)  = withContext(Dispatchers.Default){
        val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
          provideRemoteAPI.uploadAvatar(token,idUser,image)
    }
    suspend fun updateUser(user: User):ApiResponse<User> = withContext(Dispatchers.Default){
        val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
        provideRemoteAPI.updateUser(token, user)
    }
}