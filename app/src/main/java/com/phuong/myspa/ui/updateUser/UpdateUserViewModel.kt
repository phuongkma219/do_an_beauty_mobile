package com.phuong.myspa.ui.updateUser

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.phuong.myspa.base.BaseLoadingDataViewModel
import com.phuong.myspa.data.api.model.ChangePassDTO
import com.phuong.myspa.data.api.model.remote.ApiResponse
import com.phuong.myspa.data.api.model.user.User
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.user.GetMyUserUseCase
import com.phuong.myspa.data.repository.user.UserRepository
import com.phuong.myspa.utils.Constants
import com.phuong.myspa.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher, private val userRepository: UserRepository,
    private val updateUseCase: GetMyUserUseCase):
    BaseLoadingDataViewModel<ApiResponse<User>>(){
    var isUploadAvt = MutableLiveData<DataResponse<String>>(DataResponse.DataIdle())
    var updateUser = MutableLiveData<DataResponse<String>>(DataResponse.DataIdle())
    var isChangePass =  MutableLiveData<DataResponse<Boolean>>(DataResponse.DataIdle())
    fun getMyUser(){
        viewModelScope.launch (dispatcher){
            updateUseCase.invoke("").collect {
                dataMutableLiveData.postValue(it)
            }
        }
    }
    fun uploadAvatar(idUser:String,filePath:String){
        viewModelScope.launch (dispatcher){
            isUploadAvt.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
            val path = getPath(filePath)

             try {
                 val data =   userRepository.uploadAvatar(idUser, path!!)
                 if (data?.success == true){
                     val path = Constants.BASE_URL + data.data?.filename?.replace("\\", "/")
                     isUploadAvt.postValue(DataResponse.DataSuccess(path))
                 }
                 else{
                     isUploadAvt.postValue(DataResponse.DataError())
                 }
             }
             catch (e:Exception){
                 e.printStackTrace()
                 isUploadAvt.postValue(DataResponse.DataError())

             }


        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(dispatcher){
            updateUser.postValue(DataResponse.DataLoading(LoadingStatus.Loading))
            val data = userRepository.updateUser(user)
           try {
               if (data?.success == true){
                   updateUser.postValue(DataResponse.DataSuccess("SUCCESS"))
               }
               else{
                   updateUser.postValue(DataResponse.DataError())

               }
              
           }catch (e:Exception){
              e.printStackTrace()
               updateUser.postValue(DataResponse.DataError())
           }
        }
    }

   private fun getPath(filePath:String): MultipartBody.Part? {
        val file = File(filePath)
        val reqFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", file.name, reqFile)
    }
     fun changePass(changePassDTO: ChangePassDTO){
        viewModelScope.launch(dispatcher){
            val response = userRepository.changePass(changePassDTO)
            Log.d("kkk", "changePass: $response")
            if (response?.success == true){
                isChangePass.postValue(DataResponse.DataSuccess(true))
            }
            else{
                isChangePass.postValue(DataResponse.DataError())
            }
        }
    }

}