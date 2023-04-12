package com.phuong.myspa.data.repository.comment

import com.phuong.myspa.MyApp
import com.phuong.myspa.data.api.RemoteServices
import com.phuong.myspa.data.api.model.comment.DataComment
import com.phuong.myspa.data.api.model.comment.UploadComment
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.di.AppContext
import com.phuong.myspa.di.IoDispatcher
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.utils.SharedPreferenceUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class CommentRepository @Inject constructor(@IoDispatcher private val dispatcher: CoroutineDispatcher, private val provideRemoteAPI: RemoteServices
,@AppContext private val app: MyApp
) {
    suspend fun uploadComment( uploadComment:UploadComment): ApiResponse<Any>
    ?{
        return try {
            withContext(dispatcher){
                val token = Constants.PREFIX_TOKEN + SharedPreferenceUtils.getInstance(app).getData()!!.access_token
                provideRemoteAPI.uploadComment(token, uploadComment)
            }
        }
        catch (ex : UnknownHostException){
            null
        }
        catch (ex : Exception){
            null
        }
    }
    suspend fun getComments(id:String, page:String): ApiResponse<DataComment>
    ?{
        return try {
            withContext(dispatcher){
                provideRemoteAPI.getCommentsShop(id, page)
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