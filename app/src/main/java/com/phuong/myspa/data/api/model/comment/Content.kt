package com.phuong.myspa.data.api.model.comment

data class Content(var image :Array<String>? = null,var video :Array<String>? = null,var text:String,var type:String ? = "TEXT")