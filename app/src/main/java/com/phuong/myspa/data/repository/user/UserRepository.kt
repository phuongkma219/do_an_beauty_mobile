package com.phuong.myspa.data.repository.user

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.user.ImageUpload
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.net.UnknownHostException
import javax.inject.Inject

class UserRepository @Inject constructor(@IoDispatcher
private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices,@AppContext private val app: MyApp) {
    suspend fun getMyUser(): ApiResponse<User>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.getMyUser(token)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }

    suspend fun uploadAvatar(idUser:String,image: MultipartBody.Part):ApiResponse<ImageUpload>
    ?{
        return try {
            withContext(Dispatchers.Default){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.uploadAvatar(token,idUser,image)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }

    suspend fun updateUser(user: User):ApiResponse<User>
    ?{
        return try {
            withContext(Dispatchers.Default){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.updateUser(token, user)
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