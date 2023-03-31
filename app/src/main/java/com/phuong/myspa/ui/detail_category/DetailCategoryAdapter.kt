package com.phuong.myspa.ui.detail_category

import com.phuong.myspa.base.adapter.BaseAdapter
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.shop.ShopInfor

class DetailCategoryAdapter: BaseAdapter<ShopInfor>(R.layout.item_shop ) {
    interface IOnItemClickShop:BaseListener{
        fun onItemClick(item: ShopInfor, position:Int)
    }
}