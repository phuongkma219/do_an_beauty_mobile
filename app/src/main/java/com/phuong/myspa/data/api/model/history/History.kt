package com.phuong.myspa.data.api.model.history

import com.phuong.myspa.data.api.model.shop.ShopService

data class History(var _id:String,var shopName: String,var service: ShopService,var history_type:HistoryType,var time:String ="")