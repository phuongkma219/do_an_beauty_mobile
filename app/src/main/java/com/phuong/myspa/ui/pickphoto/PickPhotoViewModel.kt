package com.phuong.myspa.ui.pickphoto

import androidx.lifecycle.*
import com.hola360.lwac.ui.pickphoto.data.PickImageStatus
import com.hola360.lwac.ui.pickphoto.data.PickModelDataType
import com.hola360.lwac.ui.pickphoto.data.PickPhotoModel
import com.phuong.myspa.MyApp
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.data.api.response.DataResponse
import com.phuong.myspa.data.api.response.LoadingStatus
import com.phuong.myspa.data.repository.MediaStoreRepository
import com.phuong.myspa.di.AppContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PickPhotoViewModel  @Inject constructor(@AppContext app: MyApp) : ViewModel() {
    private val repository = MediaStoreRepository(app)
    val allImages = mutableListOf<PhotoModel>()
    val mPickPhotoModel = MutableLiveData<DataResponse<PickPhotoModel>>()
    private var curAlbumId = -1L
    var saveStatus = MutableLiveData(PickImageStatus.IDLE)
    val isExitDialog = MutableLiveData(false)
    init {
        mPickPhotoModel.value = DataResponse.DataIdle()
    }

    val isLoading: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Loading
    }
    val isEmpty: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Error
    }
    val isAlbum: LiveData<Boolean> = Transformations.map(mPickPhotoModel) {
        if (it.loadingStatus== LoadingStatus.Success) {
            val pickPhotoModel = (mPickPhotoModel.value as DataResponse.DataSuccess).body
            pickPhotoModel.pickModelDataType == PickModelDataType.LoadDetailAlbum
        } else {
            false
        }
    }

    val isSaving: LiveData<Boolean> = Transformations.map(saveStatus) {
        saveStatus.value!! == PickImageStatus.SAVING
    }
    fun onClose() {
        if (mPickPhotoModel.value!!.loadingStatus == LoadingStatus.Success) {
            val pickPhotoModel = (mPickPhotoModel.value as DataResponse.DataSuccess).body
            if (pickPhotoModel.pickModelDataType == PickModelDataType.LoadAlbum){
                loadingALl(curAlbumId)
            }else{
                isExitDialog.value = true
            }
        } else {
            isExitDialog.value = true
        }
    }
    fun loadingAlbum() {
        if (mPickPhotoModel.value !is DataResponse.DataLoading) {
            if (mPickPhotoModel.value is DataResponse.DataSuccess) {
                val body = (mPickPhotoModel.value as DataResponse.DataSuccess).body
                if (body.pickModelDataType == PickModelDataType.LoadDetailAlbum) {
                    mPickPhotoModel.value =DataResponse.DataLoading(LoadingStatus.Loading)
                    viewModelScope.launch {
                        if (allImages.isNotEmpty()) {
                            val pickPhotoModel =
                                PickPhotoModel(PickModelDataType.LoadAlbum,
                                    repository.getAlbums(curAlbumId, allImages)
                                )
                            mPickPhotoModel.value =
                                DataResponse.DataSuccess(pickPhotoModel)
                        } else {
                            mPickPhotoModel.value = DataResponse.DataError()
                        }
                    }
                } else {
                    loadingALl(curAlbumId)
                }
            }

        }
    }

    fun loadingALl(albumId: Long) {
        if (mPickPhotoModel.value!!.loadingStatus != LoadingStatus.Loading) {
            curAlbumId = albumId
            mPickPhotoModel.value = DataResponse.DataLoading(LoadingStatus.Loading)
            viewModelScope.launch() {
                if (allImages.isEmpty()) {
                    val images = repository.getImages()
                    if (images.isNotEmpty()) {
                        allImages.addAll(images)
                    }
                }
                    if (allImages.isNotEmpty()) {
                        val pickPhotoModel = if (albumId == -1L) {
                            PickPhotoModel( PickModelDataType.LoadDetailAlbum,allImages)
                        } else {
                            PickPhotoModel( PickModelDataType.LoadDetailAlbum,
                                repository.getAlbumDetail(allImages, albumId)
                            )
                        }
                        mPickPhotoModel.value = (DataResponse.DataSuccess(pickPhotoModel))
                    } else {
                        mPickPhotoModel.value = (DataResponse.DataError())
                    }

                    }
                }

        }





}