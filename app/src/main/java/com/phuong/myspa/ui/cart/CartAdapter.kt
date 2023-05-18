package com.phuong.myspa.ui.cart

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.hola.ringtonmaker.ui.base.adapter.base.BaseViewHolder
import com.phuong.myspa.MyApp
import com.phuong.myspa.R
import com.phuong.myspa.base.adapter.BaseAdapter
import com.phuong.myspa.data.api.model.shop.DataCart
import com.phuong.myspa.databinding.ItemCartBinding

class CartAdapter : BaseAdapter<DataCart>(R.layout.item_cart){
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = holder.binding as ItemCartBinding
        val mAdapter =  ItemServiceAdapter()
        item.rvProduct.adapter = mAdapter
        item.rvProduct.layoutManager = LinearLayoutManager(MyApp.resource().context)
        mAdapter.submit(list[position].items)
    }
}