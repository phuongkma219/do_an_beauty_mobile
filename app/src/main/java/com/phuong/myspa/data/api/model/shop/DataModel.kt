package com.phuong.myspa.data.api.model.shop

sealed class DataModel {
    data class DataHeader(var _id: String, var name: String) : DataModel()
    data class DataItem(
                             val _id: String,
                             var avatar: String,
                             val name: String,
                             val price: String,
                             val shop_id: String,
                             val time: String,
                             val description: String,
                             val created_at: String
                             ) : DataModel()

}
