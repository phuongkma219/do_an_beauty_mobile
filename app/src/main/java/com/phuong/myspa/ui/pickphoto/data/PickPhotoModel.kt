package com.hola360.lwac.ui.pickphoto.data

import com.phuong.myspa.data.PhotoModel


data class PickPhotoModel(
    val pickModelDataType: PickModelDataType, val photoList: MutableList<PhotoModel>
)