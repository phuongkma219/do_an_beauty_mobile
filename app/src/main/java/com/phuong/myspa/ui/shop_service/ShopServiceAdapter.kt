package com.phuong.myspa.ui.shop_service

import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.data.api.model.shop.ShopService

class ShopServiceAdapter: BaseAdapter<ShopService>(R.layout.item_shop_service) {
    interface IOnItemClickService: BaseListener {
        fun onItemClick(item: ShopService, position:Int)
    }}