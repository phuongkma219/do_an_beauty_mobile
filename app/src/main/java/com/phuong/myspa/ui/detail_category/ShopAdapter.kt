package com.phuong.myspa.ui.detail_category

import android.view.View
import com.phuong.myspa.base.adapter.BaseAdapter
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.hola.ringtonmaker.ui.base.adapter.base.BaseViewHolder
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.databinding.ItemShopBinding

class ShopAdapter(private val showAction :Boolean): BaseAdapter<ShopInfor>(R.layout.item_shop ) {
    interface IOnItemClickShop:BaseListener{
        fun onItemClick(item: ShopInfor, position:Int)
        fun onItemMoreAction(view:View,item: ShopInfor, position:Int)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = holder.binding as ItemShopBinding
        if (showAction){
            item.ivMoreAction.visibility = View.VISIBLE
        }
        else{
            item.ivMoreAction.visibility = View.GONE
        }
        item.ivMoreAction.setOnClickListener {
            (listener as IOnItemClickShop).onItemMoreAction(it,list[position],position)
        }
    }
}