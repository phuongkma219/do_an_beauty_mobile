package com.phuong.myspa.data.api.model.history

import android.os.Parcelable
import com.phuong.myspa.data.api.model.shop.ShopService
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(var _id:String,var shopName: String,var service: ShopService,var history_type:HistoryType,var time:String? = null):Parcelable