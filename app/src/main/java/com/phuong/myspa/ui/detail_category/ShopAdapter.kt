package com.phuong.myspa.ui.detail_category

import android.view.View
import com.phuong.myspa.base.adapter.BaseAdapter
import com.hola.ringtonmaker.ui.base.adapter.base.BaseListener
import com.phuong.myspa.base.adapter.BaseViewHolder
import com.phuong.myspa.R
import com.phuong.myspa.data.api.model.search.Search
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
    fun submitList(isAddMore: Boolean, newData: MutableList<ShopInfor>) {
        if (isAddMore) {
            this.list.addAll(newData)
            notifyItemInserted(list.size)
        } else {
            this.list = newData.toMutableList()
            notifyDataSetChanged()
        }

    }
    fun submitListSearch(isAddMore: Boolean, newData: MutableList<Search>) {
        val data = mutableListOf<ShopInfor>()
        newData.forEach {
            data.add(it.shop)
        }
        if (isAddMore) {
            this.list.addAll(data)
            notifyItemInserted(list.size)
        } else {
            this.list = data
            notifyDataSetChanged()
        }

    }
    fun clearData(){
        list.clear()
        notifyDataSetChanged()
    }
    fun deleteItem(position:Int){
        list.removeAt(position)
        notifyItemRemoved(position)
    }
}