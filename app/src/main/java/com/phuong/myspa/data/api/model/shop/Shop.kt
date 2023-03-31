package com.phuong.myspa.data.api.model.shop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Shop(var infor: ShopInfor, var service : MutableList<ShopService>):Parcelable