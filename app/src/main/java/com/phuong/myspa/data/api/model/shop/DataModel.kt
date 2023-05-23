package com.phuong.myspa.data.api.model.shop

sealed class DataModel {
    data class DataHeader(   var __v: Int,
                             var _id: String,
                             var address: String,
                             var avatar: String,
                             var category: List<String>,
                             var created_at: String,
                             var description: String,
                             var email: String,
                             var end_time: String,
                             var name: String,
                             var phone_number: String,
                             var rate: Double,
                             var start_time: String,
                             var updated_at: String) : DataModel()
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
